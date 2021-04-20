package com.hy.account.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.additional.update.impl.LambdaUpdateChainWrapper;
import com.hy.account.mapper.AccountMapper;
import com.hy.account.mapper.AccountTypeMapper;
import com.hy.account.model.Account;
import com.hy.account.model.vo.AccountVO;
import com.hy.account.service.IAccountService;
import com.hy.center.model.FinancialRecords;
import com.hy.center.model.bo.FinancialRecordsBO;
import com.hy.center.service.ICenterStageService;
import com.hy.constant.Constant;
import com.hy.enums.AccountLeve;
import com.hy.enums.AccountStatus;
import com.hy.enums.OrderType;
import com.hy.enums.PayMethod;
import com.hy.order.model.bo.OrderBO;
import com.hy.order.service.IOrderService;
import com.hy.pojo.ResponseJsonResult;
import com.hy.user.model.User;
import com.hy.user.service.IUserService;
import com.hy.utils.MD5Utils;
import com.hy.utils.RedisOperator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.hy.constant.Constant.*;
import static com.hy.enums.AccountLeve.ONE;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/12/29 17:22
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/12/29 17:22
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@RestController
@Slf4j
public class AccountServiceImpl implements IAccountService {

    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private RedisOperator redisOperator;
    @Autowired
    private AccountTypeMapper accountTypeMapper;
    @Autowired
    private IOrderService iOrderService;
    @Autowired
    private ICenterStageService iCenterStageService;
    @Autowired
    private IUserService iUserService;
    @Autowired
    private RedissonClient redissonClient;



    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResponseJsonResult submitOrderTask(String userId, Long orderId, Double rewarsAmount) {

        Account account = getAccountInfo(userId);
        if (null == account) {
            throw new RuntimeException("Account Anomaly！");
        }
        new LambdaUpdateChainWrapper<>(accountMapper)
                .eq(Account::getUserId, userId)
                .eq(Account::getAccountLeve, ONE.id)
                .eq(Account::getAccountStatus, AccountStatus.NORMAL.type)
                .set(Account::getWalletAccount, account.getWalletAccount() + rewarsAmount)
                .set(Account::getDayIncomeAccount, account.getDayIncomeAccount() + rewarsAmount)
                .set(Account::getUpdateTime, LocalDateTime.now()).update();

        iOrderService.updateOrderInfo(orderId, 30, "success", "Wallet -> " + "Order Task");

        return ResponseJsonResult.ok();
    }

    //体验金清零
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void experienceZero(String userId) {
        new LambdaUpdateChainWrapper<>(accountMapper)
                .eq(Account::getUserId, userId)
                .eq(Account::getAccountLeve, ONE.id)
                .eq(Account::getAccountStatus, AccountStatus.NORMAL.type)
                .set(Account::getVirtualAccount, 0)
                .set(Account::getUpdateTime, LocalDateTime.now()).update();
    }


    //今日收益清零
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void zeroReturnToday(String userId) {
        new LambdaUpdateChainWrapper<>(accountMapper)
                .eq(Account::getUserId, userId)
                .eq(Account::getAccountLeve, ONE.id)
                .eq(Account::getAccountStatus, AccountStatus.NORMAL.type)
                .set(Account::getDayIncomeAccount, 0)
                .set(Account::getUpdateTime, LocalDateTime.now()).update();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Account createDefaultAccount(String userId) {

        Account account = null;
        try {
            account = Account.builder().userId(userId)
                    .virtualAccount(VIRTUAL_EXPERIENCE_GOLD)
                    .accountLeve(ONE.id)
                    .accountNo(System.currentTimeMillis())
                    .accountPassword(MD5Utils.getMD5Str(Constant.DEFAULT_PASSWORD))
                    .accountStatus(AccountStatus.NORMAL.type)
                    .createTime(new Date())
                    .updateTime(new Date()).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        accountMapper.insert(account);
        return account;
    }


    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResponseJsonResult transferIn(String orderToken, String userId, Integer productId, Double amount) {

        ResponseJsonResult x = checkParams(userId, productId, amount);
        if (x != null) return x;
        //校验订单token
        verifyOrderToken(orderToken, userId);

        FinancialRecords financialRecords = iCenterStageService.getFinancialRecords(userId, productId);
        if (financialRecords != null) {
            return ResponseJsonResult.errorMsg("Has purchased the wealth management product");
        }
        Account account = getAccountInfo(userId);
        if (null == account) {
            return ResponseJsonResult.errorMsg("Account Anomaly！");
        }

        new LambdaUpdateChainWrapper<>(accountMapper)
                .eq(Account::getUserId, userId)
                .eq(Account::getAccountLeve, ONE.id)
                .eq(Account::getAccountStatus, AccountStatus.NORMAL.type)
                .set(Account::getWalletAccount, account.getWalletAccount() - amount)
                .set(Account::getFrozenAccounts, account.getFrozenAccounts() + amount)
                .set(Account::getFinancialAccount, account.getFinancialAccount() + amount)
                .set(Account::getUpdateTime, LocalDateTime.now()).update();


        //订单系统
        Long orderId = generateSystemOrderFlow(userId, amount, PayMethod.TRANSFER_PAY.id, OrderType.TRANSFER_IN.id);
        iOrderService.updateOrderInfo(orderId, 30, "success", "Wallet -> " + "Financial products");
        //消息队列，T+1 结算
        User user = iUserService.queryUserInfoByUserId(userId);
        assert user != null;
        String name = user.getNicknames();
        FinancialRecordsBO financialRecordsBO = FinancialRecordsBO.builder()
                .amount(amount)
                .nicknames(name)
                .orderId(orderId)
                .productId(productId)
                .userId(userId)
                .build();
        iCenterStageService.createFinancialRecordsInfo(financialRecordsBO);
        return ResponseJsonResult.build(SUCCESS_CODE, "Transfer in successful !", "");
    }

    private void verifyOrderToken(String orderToken, String userId) {
        //校验订单token状态，查询Redis缓存
        String orderTokenKey = REDIS_ORDER_TOKEN + ":" + userId;
        String lockKey = REDIS_LOCK_ORDER_FINANCIAL + ":" + userId;
        RLock rLock = redissonClient.getLock(lockKey);
        rLock.lock(5, TimeUnit.SECONDS);
        try {
            String order_token = redisOperator.get(orderTokenKey);
            log.info("******************************订单token为***********************{}", order_token);
            if (StringUtils.isBlank(orderToken)) {
                throw new RuntimeException("orderToken不存在");
            }
            boolean correctToken = order_token.equals(orderToken.trim());
            if (!correctToken) {
                throw new RuntimeException("orderToken不正确");
            }
            redisOperator.del(orderTokenKey);
        } finally {
            try {
                rLock.unlock();
            } catch (Exception ignored) {

            }
        }
    }

    private ResponseJsonResult checkParams(String userId, Integer productId, Double amount) {
        if (StringUtils.isBlank(userId)) {
            return ResponseJsonResult.errorMsg("User userId cannot be empty!");
        }
        if (StringUtils.isBlank(String.valueOf(productId))) {
            return ResponseJsonResult.errorMsg("productId cannot be empty!");
        }

        if (StringUtils.isBlank(String.valueOf(amount))) {
            return ResponseJsonResult.errorMsg("amount cannot be empty!");
        }

        //校验用户登录状态，查询Redis缓存
        String str = redisOperator.get(Constant.REDIS_USER_TOKEN + ":" + userId);
        if (str == null) {
            return ResponseJsonResult.build(LOGIN_EXPIRED_CODE, "Login expired, please login again !", "");
        }
        return null;
    }

    //理财产品T+1 结算
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean financialSettlementOfTPlusOne(String userId, Double dailyInterestAmount) {
        Account account = getAccountInfo(userId);
        if (null == account) {
            throw new RuntimeException("Account Anomaly！");
        }

        Long orderId = generateSystemOrderFlow(userId, dailyInterestAmount, PayMethod.SYSTEM_PAY.id, OrderType.DAILY_INTEREST.id);
        log.info("理财日息T+1 结算，当前系统订单号为："+orderId);

        return new LambdaUpdateChainWrapper<>(accountMapper)
                .eq(Account::getUserId, userId)
                .eq(Account::getAccountLeve, ONE.id)
                .eq(Account::getAccountStatus, AccountStatus.NORMAL.type)
                .set(Account::getWalletAccount, account.getWalletAccount() + dailyInterestAmount)
                .set(Account::getUpdateTime, new Date()).update();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResponseJsonResult transferOut(String orderToken, String userId, Integer productId, Double amount) {

        ResponseJsonResult x = checkParams(userId, productId, amount);
        if (x != null) return x;

        verifyOrderToken(orderToken, userId);


        Account account = getAccountInfo(userId);
        if (null == account) {
            return ResponseJsonResult.errorMsg("Account Anomaly！");
        }
        if (account.getFinancialAccount() < amount) {
            return ResponseJsonResult.errorMsg("Sorry, your credit is running low");
        }

        ResponseJsonResult result = iCenterStageService.updateFinancialRecordsInfo(userId, productId, amount);
        if (!result.getStatus().equals(SUCCESS_CODE)) {
            return result;
        }

        new LambdaUpdateChainWrapper<>(accountMapper)
                .eq(Account::getUserId, userId)
                .eq(Account::getAccountLeve, ONE.id)
                .eq(Account::getAccountStatus, AccountStatus.NORMAL.type)
                .set(Account::getWalletAccount, account.getWalletAccount() + amount)
                .set(Account::getFrozenAccounts, account.getFrozenAccounts() - amount)
                .set(Account::getFinancialAccount, account.getFinancialAccount() - amount)
                .set(Account::getUpdateTime, LocalDateTime.now()).update();

        Long orderId = generateSystemOrderFlow(userId, amount, PayMethod.TRANSFER_PAY.id, OrderType.TRANSFER_OUT.id);
        iOrderService.updateOrderInfo(orderId, 30, "success", "Financial products -> " + "Wallet");

        return ResponseJsonResult.build(SUCCESS_CODE, "Transfer out successful !", "");
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ResponseJsonResult queryMyAccountInfo(String userId) {
        ResponseJsonResult x = checkParams(userId, null, null);
        if (x != null) return x;

        User user = iUserService.queryUserInfoByUserId(userId);
        Account account = getAccountInfo(userId);
        if (null == account) {
            return ResponseJsonResult.errorMsg("Account Anomaly！");
        }

        AccountVO accountVO = AccountVO.builder()
                .dayIncomeAccount(account.getDayIncomeAccount())
                .financialAccount(account.getFinancialAccount())
                .frozenAccounts(account.getFrozenAccounts())
                .personalIncomeAccount(account.getPersonalIncomeAccount())
                .teamContributionAccount(account.getTeamContributionAccount())
                .virtualAccount(account.getVirtualAccount())
                .availableAssets(account.getWalletAccount())
                .teamSize(user.getTeamSize())
                .build();

        return ResponseJsonResult.ok(accountVO);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Account getAccountInfo(String userId) {
        QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("account_leve", AccountLeve.ONE.id);
        return accountMapper.selectOne(queryWrapper);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean increaseRewards(String userId, Double rewarsAmount) {

        Account account = getAccountInfo(userId);
        if (null == account) {
            throw new RuntimeException("Account Anomaly！");
        }
        return new LambdaUpdateChainWrapper<>(accountMapper)
                .eq(Account::getUserId, userId)
                .eq(Account::getAccountLeve, ONE.id)
                .eq(Account::getAccountStatus, AccountStatus.NORMAL.type)
                .set(Account::getWalletAccount, account.getWalletAccount() + rewarsAmount)
                .set(Account::getPersonalIncomeAccount, account.getPersonalIncomeAccount() + rewarsAmount)
                .set(Account::getUpdateTime, LocalDateTime.now()).update();
    }


    //生成系统订单流水
    private Long generateSystemOrderFlow(String userId, Double amount, Integer payMethod, Integer orderType) {
        // 调用订单中心，创建系统订单流水信息
        OrderBO orderBO = OrderBO.builder()
                .userId(userId)
                .amount(amount)
                .realPayAmount(amount)
                .commissionRate(0.00)
                .commissionRewarsAmount(0.00)
                .imgUrl(null)
                .discountRate(0)
                .orderType(orderType)
                .payMethod(payMethod)
                .build();
        return iOrderService.createOrderInfo(orderBO);
    }

}
