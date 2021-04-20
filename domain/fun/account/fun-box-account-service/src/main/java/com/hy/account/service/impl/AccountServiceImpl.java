package com.hy.account.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.additional.update.impl.LambdaUpdateChainWrapper;
import com.hy.account.mapper.AccountMapper;
import com.hy.account.mapper.AccountTypeMapper;
import com.hy.account.model.Account;
import com.hy.account.model.AccountType;
import com.hy.account.model.SecondaryAccountInfo;
import com.hy.account.model.vo.TotalInvitationGiftAndRebateRewardVO;
import com.hy.account.model.vo.TotalInvitationRewardAndCommissionRewardVO;
import com.hy.account.service.IAccountService;
import com.hy.constant.Constant;
import com.hy.enums.*;
import com.hy.lottery.service.IBetRecordService;
import com.hy.order.model.bo.OrderBO;
import com.hy.order.service.IOrderService;
import com.hy.pojo.ResponseJsonResult;
import com.hy.utils.MD5Utils;
import com.hy.utils.RedisOperator;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static com.hy.constant.Constant.LOGIN_EXPIRED_CODE;


/**
 * @Description: $- AccountServiceImpl 账户服务业务处理-$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/11/19 15:50
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/11/19 15:50
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
    private IOrderService iOrderService;
    @Autowired
    private IBetRecordService iBetRecordService;
    @Autowired
    private AccountTypeMapper accountTypeMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ResponseJsonResult queryAccountTypeInfoList(String userId) {

        if(StringUtil.isBlank(userId)){
            return ResponseJsonResult.errorMsg("User ID cannot be empty!");
        }
        String str = redisOperator.get(Constant.REDIS_USER_TOKEN + ":" + userId);
        if (str == null) {
            return ResponseJsonResult.build(LOGIN_EXPIRED_CODE,"Login expired, please login again !","");
        }
        List<AccountType> accountTypeList = getAccountTypeInfoList();
        accountTypeList.removeIf(accountType -> accountType.getId() == 0);
        return ResponseJsonResult.ok(accountTypeList);
    }

    //获取账户类型信息
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<AccountType> getAccountTypeInfoList() {
        QueryWrapper<AccountType> queryWrapper = new QueryWrapper<>();
        return accountTypeMapper.selectList(queryWrapper);
    }

    //转账  （转入）
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResponseJsonResult transferIn(String userId, Integer accountType, Double amount) {

        if(StringUtil.isBlank(userId)){
            return ResponseJsonResult.errorMsg("User ID cannot be empty!");
        }
        if(StringUtil.isBlank(String.valueOf(accountType))){
            return ResponseJsonResult.errorMsg("accountType cannot be empty!");
        }
        if(StringUtil.isBlank(String.valueOf(amount))){
            return ResponseJsonResult.errorMsg("amount cannot be empty!");
        }
        String str = redisOperator.get(Constant.REDIS_USER_TOKEN + ":" + userId);
        if (str == null) {
            return ResponseJsonResult.build(LOGIN_EXPIRED_CODE,"Login expired, please login again !","");
        }

        Double account_blance = getAccountBlance(userId);

        if (account_blance > 0 && account_blance > amount) {

            //扣减钱包中心余额
            new LambdaUpdateChainWrapper<>(accountMapper)
                    .eq(Account::getUserId, userId)
                    .eq(Account::getAccountLeve, AccountLeve.ONE.id)
                    .eq(Account::getAccountType, 0)
                    .eq(Account::getAccountStatus, AccountStatus.NORMAL.type)
                    .set(Account::getAccountBlance, account_blance - amount)
                    .set(Account::getUpdateTime, LocalDateTime.now()).update();

            //获取二级账户余额
            Double second_account_blance = getSecondaryAccountBlance(userId, accountType);

            //增加余额到二级账户
            new LambdaUpdateChainWrapper<>(accountMapper)
                    .eq(Account::getUserId, userId)
                    .eq(Account::getAccountLeve, AccountLeve.TWO.id)
                    .eq(Account::getAccountType, accountType)
                    .eq(Account::getAccountStatus, AccountStatus.NORMAL.type)
                    .set(Account::getAccountBlance, second_account_blance + amount)
                    .set(Account::getUpdateTime, LocalDateTime.now()).update();

            //生成订单流水
            // 调用订单中心，创建系统订单流水信息
            Long orderId = generateSystemOrderFlow(userId, amount, PayMethod.TRANSFER_PAY.id, OrderType.TRANSFER_IN.id);

            List<AccountType> accountTypeList = getAccountTypeInfoList();
            String accountTypeName="";
            for (AccountType at : accountTypeList) {
                if (accountType.equals(at.getId())) {
                    accountTypeName = at.getName();
                }
            }
            iOrderService.updateOrderInfo(orderId, 30, "success","Platfrom -> "+accountTypeName);

        } else {
            return ResponseJsonResult.errorMsg("Insufficient balance in wallet Center");
        }
        return ResponseJsonResult.build(200,"Transfer successful","");
    }

    //查询锁定余额
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ResponseJsonResult lockWallet(String userId){
        if(StringUtil.isBlank(userId)){
            return ResponseJsonResult.errorMsg("User ID cannot be empty!");
        }

        String str = redisOperator.get(Constant.REDIS_USER_TOKEN + ":" + userId);
        if (str == null) {
            return ResponseJsonResult.build(LOGIN_EXPIRED_CODE,"Login expired, please login again !","");
        }
        QueryWrapper<Account> queryWrapper = new QueryWrapper<Account>();
        queryWrapper.select("sum(account_blance) accountBlance")
                .eq("account_leve", AccountLeve.TWO.id)
                .and(u -> u.eq("user_id", userId));
        Account account = accountMapper.selectOne(queryWrapper);
        return ResponseJsonResult.ok(account.getAccountBlance());
    }

    //检索所有余额  (转出)
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ResponseJsonResult transferOut(String userId){
        if(StringUtil.isBlank(userId)){
            return ResponseJsonResult.errorMsg("User ID cannot be empty!");
        }

        String str = redisOperator.get(Constant.REDIS_USER_TOKEN + ":" + userId);
        if (str == null) {
            return ResponseJsonResult.build(LOGIN_EXPIRED_CODE,"Login expired, please login again !","");
        }

        //查询平台账户余额
        Double account_blance = getAccountBlance(userId);

        List<AccountType> accountTypeList = getAccountTypeInfoList();
        accountTypeList.removeIf(accountType -> accountType.getId() == 0);
        accountTypeList.forEach(accountType -> {
            //获取二级账户余额
            Double second_account_blance = getSecondaryAccountBlance(userId, accountType.getId());


            // 扣除二级账户余额为 0
            new LambdaUpdateChainWrapper<>(accountMapper)
                    .eq(Account::getUserId, userId)
                    .eq(Account::getAccountLeve, AccountLeve.TWO.id)
                    .eq(Account::getAccountType, accountType)
                    .eq(Account::getAccountStatus, AccountStatus.NORMAL.type)
                    .set(Account::getAccountBlance, 0)
                    .set(Account::getUpdateTime, LocalDateTime.now()).update();

            //将二级账户的余额增加到平台账户
            new LambdaUpdateChainWrapper<>(accountMapper)
                    .eq(Account::getUserId, userId)
                    .eq(Account::getAccountLeve, AccountLeve.ONE.id)
                    .eq(Account::getAccountType, 0)
                    .eq(Account::getAccountStatus, AccountStatus.NORMAL.type)
                    .set(Account::getAccountBlance, account_blance + second_account_blance)
                    .set(Account::getUpdateTime, LocalDateTime.now()).update();

            //生成订单流水
            // 调用订单中心，创建系统订单流水信息
            Long orderId = generateSystemOrderFlow(userId, second_account_blance, PayMethod.TRANSFER_PAY.id, OrderType.TRANSFER_OUT.id);

            iOrderService.updateOrderInfo(orderId, 30, "success",accountType.getName()+" -> Platfrom");

        });
        return ResponseJsonResult.ok(getAccountBlance(userId));

    }



    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Account createDefaultAccount(String userId) {

        Account account = null;

        //创建各个平台子账户
        List<AccountType> accountTypeList = getAccountTypeInfoList();
        for (AccountType accountType : accountTypeList) {
            int account_leve;
            if (accountType.getId() == 0) {
                account_leve = AccountLeve.ONE.id;
            } else {
                account_leve = AccountLeve.TWO.id;
            }
            try {

                account = Account.builder().userId(userId)
                        .accountBlance(Constant.DEFAULT_BLANCE)
                        .accountLeve(account_leve)
                        .accountType(accountType.getId())
                        .accountTypeName(accountType.getName())
                        .accountNo(System.currentTimeMillis())
                        .accountPassword(MD5Utils.getMD5Str(Constant.DEFAULT_PASSWORD))
                        .accountStatus(AccountStatus.NORMAL.type)
                        .createTime(new Date())
                        .updateTime(new Date()).build();

                accountMapper.insert(account);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return account;
    }

    @Override
    public ResponseJsonResult accountRecharge(String userId, Double amount, Long accountId, Integer payMethod, Integer orderType) {
        String str = redisOperator.get(Constant.REDIS_USER_TOKEN + ":" + userId);
        if (str == null) {
            return ResponseJsonResult.build(LOGIN_EXPIRED_CODE,"Login expired, please login again !","");
        }
        Double blance = toRecharge(userId, amount, payMethod, orderType);

        return ResponseJsonResult.build(200, "Account recharged successfully!", blance);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public ResponseJsonResult modifyAccountPassword(Long accountId, String userId, String newAccountPassword) {
        //验证用户
        //校验用户登录状态，查询Redis缓存
        String str = redisOperator.get(Constant.REDIS_USER_TOKEN + ":" + userId);
        if (str == null) {
            return ResponseJsonResult.build(LOGIN_EXPIRED_CODE,"Login expired, please login again !","");
        }
        Account account = Account.builder().accountId(accountId).accountPassword(newAccountPassword).updateTime(new Date()).build();
        accountMapper.updateById(account);
        return ResponseJsonResult.build(200, "Payment password modified successfully!", "");

    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Double toRecharge(String userId, Double amount, Integer payMethod, Integer orderType) {

        try {
            // 调用订单中心，创建系统订单流水信息
            Long orderId = generateSystemOrderFlow(userId, amount, payMethod, orderType);

            UpdateWrapper<Account> accountUpdateWrapper = new UpdateWrapper<>();
            accountUpdateWrapper.eq("user_id", userId).eq("account_leve", AccountLeve.ONE.id).eq("account_type", 0);
            Account account = Account.builder()
                    .accountBlance(getAccountBlance(userId) + amount)
                    .updateTime(new Date())
                    .build();
            int result = accountMapper.update(account, accountUpdateWrapper);
            if (result == 0) {
                throw new RuntimeException("数据异常");
            }

            iOrderService.updateOrderInfo(orderId, 30, "success","");


        } catch (Exception e) {
            e.printStackTrace();
        }

        return getAccountBlance(userId);
    }

    //充值返利
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void rechargeRebate(String userId) {

        double recharge_amount = iOrderService.totalRechargeAmount(userId) * 0.05;

        Account account = getAccountInfo(userId);
        Long orderId = generateSystemOrderFlow(userId, recharge_amount, 5, 10);
        boolean result = new LambdaUpdateChainWrapper<>(accountMapper)
                .eq(Account::getUserId, userId)
                .eq(Account::getAccountStatus, AccountStatus.NORMAL.type)
                .eq(Account::getAccountLeve, AccountLeve.ONE.id)
                .eq(Account::getAccountType, 0)
                .set(Account::getRechargeRebateBlance, account.getRechargeRebateBlance() + recharge_amount)
                .set(Account::getUpdateTime, LocalDateTime.now()).update();
        if (!result) {
            throw new RuntimeException("数据异常");
        }
        iOrderService.updateOrderInfo(orderId, 30, "success","success");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Boolean signInReward(String userId, Double reward_amount, Integer payMethod, Integer orderType) {
        Account account = getAccountInfo(userId);
        // 调用订单中心，创建系统订单流水信息
        Long orderId = generateSystemOrderFlow(userId, reward_amount, payMethod, orderType);
        boolean result = new LambdaUpdateChainWrapper<>(accountMapper)
                .eq(Account::getUserId, userId)
                .eq(Account::getAccountStatus, AccountStatus.NORMAL.type)
                .eq(Account::getAccountLeve, AccountLeve.ONE.id)
                .eq(Account::getAccountType, 0)
                .set(Account::getRewardAmount, account.getRewardAmount() + reward_amount)
                .set(Account::getUpdateTime, LocalDateTime.now()).update();
        if (!result) {
            throw new RuntimeException("数据异常");
        }
        iOrderService.updateOrderInfo(orderId, 30, "success","success");
        return result;
    }

    @Override
    public Boolean rechargeRebateBlance(String userId, Double rechargeRebate, Integer payMethod, Integer orderType) {
        Account account = getAccountInfo(userId);
        // 调用订单中心，创建系统订单流水信息
        Long orderId = generateSystemOrderFlow(userId, rechargeRebate, payMethod, orderType);
        boolean result = new LambdaUpdateChainWrapper<>(accountMapper)
                .eq(Account::getUserId, userId)
                .eq(Account::getAccountStatus, AccountStatus.NORMAL.type)
                .eq(Account::getAccountLeve, AccountLeve.ONE.id)
                .eq(Account::getAccountType, 0)
                .set(Account::getRechargeRebateBlance, account.getRechargeRebateBlance() + rechargeRebate)
                .set(Account::getUpdateTime, LocalDateTime.now()).update();

        if (!result) {
            throw new RuntimeException("数据异常");
        }
        iOrderService.updateOrderInfo(orderId, 30, "success","success");
        return result;
    }

    @Override
    public Boolean commissionBlance(String userId, Double commission, Integer payMethod, Integer orderType) {
        Account account = getAccountInfo(userId);
        // 调用订单中心，创建系统订单流水信息
        Long orderId = generateSystemOrderFlow(userId, commission, payMethod, orderType);
        boolean result = new LambdaUpdateChainWrapper<>(accountMapper)
                .eq(Account::getUserId, userId)
                .eq(Account::getAccountStatus, AccountStatus.NORMAL.type)
                .eq(Account::getAccountLeve, AccountLeve.ONE.id)
                .eq(Account::getAccountType, 0)
                .set(Account::getCommissionBlance, account.getCommissionBlance() + commission)
                .set(Account::getUpdateTime, LocalDateTime.now()).update();

        if (!result) {
            throw new RuntimeException("数据异常");
        }
        iOrderService.updateOrderInfo(orderId, 30, "success","success");
        return result;
    }

    @Override
    public Boolean invitationRewardBlance(String userId, Double invitationReward, Integer payMethod, Integer orderType) {
        Account account = getAccountInfo(userId);
        // 调用订单中心，创建系统订单流水信息
        Long orderId = generateSystemOrderFlow(userId, invitationReward, payMethod, orderType);
        boolean result = new LambdaUpdateChainWrapper<>(accountMapper)
                .eq(Account::getUserId, userId)
                .eq(Account::getAccountStatus, AccountStatus.NORMAL.type)
                .eq(Account::getAccountLeve, AccountLeve.ONE.id)
                .eq(Account::getAccountType, 0)
                .set(Account::getCommissionBlance, account.getInvitationRewardBlance() + invitationReward)
                .set(Account::getUpdateTime, LocalDateTime.now()).update();

        if (!result) {
            throw new RuntimeException("数据异常");
        }
        iOrderService.updateOrderInfo(orderId, 30, "success","success");
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Boolean upgradeRewardBlance(String userId, Double upgradeReward, Integer payMethod, Integer orderType) {
        Account account = getAccountInfo(userId);
        // 调用订单中心，创建系统订单流水信息
        Long orderId = generateSystemOrderFlow(userId, upgradeReward, payMethod, orderType);
        boolean result = new LambdaUpdateChainWrapper<>(accountMapper)
                .eq(Account::getUserId, userId)
                .eq(Account::getAccountStatus, AccountStatus.NORMAL.type)
                .eq(Account::getAccountLeve, AccountLeve.ONE.id)
                .eq(Account::getAccountType, 0)
                .set(Account::getUpgradeRewardBlance, account.getUpgradeRewardBlance() + upgradeReward)
                .set(Account::getUpdateTime, LocalDateTime.now()).update();

        if (!result) {
            throw new RuntimeException("数据异常");
        }
        if (!iOrderService.updateOrderInfo(orderId, 30, "success","success")) {
            throw new RuntimeException("同步系统订单状态异常");
        }
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Boolean monthlyTicketBlance(String userId, Double monthlyTicket, Integer payMethod, Integer orderType) {

        Account account = getAccountInfo(userId);
        // 调用订单中心，创建系统订单流水信息
        Long orderId = generateSystemOrderFlow(userId, monthlyTicket, payMethod, orderType);
        boolean result = new LambdaUpdateChainWrapper<>(accountMapper)
                .eq(Account::getUserId, userId)
                .eq(Account::getAccountStatus, AccountStatus.NORMAL.type)
                .eq(Account::getAccountLeve, AccountLeve.ONE.id)
                .eq(Account::getAccountType, 0)
                .set(Account::getMonthlyTicketBlance, account.getMonthlyTicketBlance() + monthlyTicket)
                .set(Account::getUpdateTime, LocalDateTime.now()).update();
        if (!result) {
            throw new RuntimeException("数据异常");
        }
        if (!iOrderService.updateOrderInfo(orderId, 30, "success","success")) {
            throw new RuntimeException("同步系统订单状态异常");
        }
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResponseJsonResult pay(String userId, Long orderIds, Double amount, String accountPassword, Integer payMethod, Integer orderType) {

        ResponseJsonResult responseJsonResult;
        //校验账户状态
        responseJsonResult = checkUserAccountStatus(userId);
        if (responseJsonResult.getStatus() != 200) {
            return responseJsonResult;
        }
        //校验账户支付密码
        responseJsonResult = checkUserAccountPassword(userId, accountPassword);
        if (responseJsonResult.getStatus() != 200) {
            return responseJsonResult;
        }
        //生成系统订单流水
        Long orderId = generateSystemOrderFlow(userId, amount, payMethod, orderType);

        //payMethod 2  账户余额支付
        if (payMethod == 2) {
            accountPayment(userId, amount, orderId);
        }
        // UPI支付
        if (payMethod == 1) {
            //TODO
        }

        iOrderService.updateOrderInfo(orderId, 30, "success","success");
        //同步支付记录
        if (orderType == 1) {
            responseJsonResult = iBetRecordService.updateBetRecordPayStatus(orderIds, userId);
            if (responseJsonResult.getStatus() != 200) {
                return responseJsonResult;
            } else {
                return ResponseJsonResult.build(200, "Payment successful", "");
            }
        }
        return ResponseJsonResult.build(200, "Payment successful", "");
    }

    //生成系统订单流水
    private Long generateSystemOrderFlow(String userId, Double amount, Integer payMethod, Integer orderType) {
        // 调用订单中心，创建系统订单流水信息
        OrderBO orderBO = OrderBO.builder()
                .userId(userId)
                .amount(amount)
                .discountRate(0)
                .orderType(orderType)
                .platformId(Platfrom.SYSTEM_SERVICE.id)
                .platformName(Platfrom.SYSTEM_SERVICE.name)
                .payMethod(payMethod)
                .realPayAmount(amount)
                .build();
        return iOrderService.createOrderInfo(orderBO);
    }

    //账户支付
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResponseJsonResult accountPayment(String userId, Double amount, Long orderId) {

        ResponseJsonResult responseJsonResult;
        //校验账户状态
        responseJsonResult = checkUserAccountStatus(userId);
        if (responseJsonResult.getStatus() != 200) {
            return responseJsonResult;
        }

        //检查用户账户余额是否足够支付
        Double accountBalance = getAccountBlance(userId);
        //如果账户余额不足，直接返回提示，停止继续执行
        if (accountBalance < 10) {
            throw new RuntimeException("Insufficient account balance");
        }

        iOrderService.updateOrderInfo(orderId, 30, "success","success");

        LambdaUpdateWrapper<Account> lambdaUpdateWrapper = Wrappers.lambdaUpdate();
        lambdaUpdateWrapper
                .eq(Account::getUserId, userId)
                .eq(Account::getUserId, userId)
                .eq(Account::getAccountStatus, AccountStatus.NORMAL.type)
                .eq(Account::getAccountType, 0)
                .eq(Account::getAccountLeve, AccountLeve.ONE.id)
                .set(Account::getAccountBlance, getAccountBlance(userId) - amount)
                .set(Account::getUpdateTime, new Date());
        accountMapper.update(null, lambdaUpdateWrapper);

        log.info("******************************账户支付成功****************************");
        return ResponseJsonResult.build(200, "Payment successful", "");
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ResponseJsonResult checkUserAccountPassword(String userId, String accountPassword) {
        if (StringUtil.isBlank(userId)) {
            return ResponseJsonResult.errorMsg("User ID cannot be empty!");
        }
        if (StringUtil.isBlank(accountPassword)) {
            return ResponseJsonResult.errorMsg("Account payment password cannot be empty!");
        }
        //校验用户登录状态，查询Redis缓存
        String str = redisOperator.get(Constant.REDIS_USER_TOKEN + ":" + userId);
        if (str == null) {
            return ResponseJsonResult.build(LOGIN_EXPIRED_CODE,"Login expired, please login again !","");
        }
        QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
        try {
            queryWrapper.eq("user_id", userId).eq("account_password", MD5Utils.getMD5Str(accountPassword)).eq("account_leve", 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Account account = accountMapper.selectOne(queryWrapper);
        if(account ==null){
            return ResponseJsonResult.errorMsg("accountPassword is error ! ");
        }
        return ResponseJsonResult.ok();

    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ResponseJsonResult checkUserAccountStatus(String userId) {

        QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        Account account = accountMapper.selectOne(queryWrapper);
        if (account.getAccountStatus().equals(AccountStatus.FROZEN.type)) {
            return ResponseJsonResult.errorMsg("The account has been frozen!");
        }
        if (account.getAccountStatus().equals(AccountStatus.CANCELLATION.type)) {
            return ResponseJsonResult.errorMsg("The account has been  cancelled!");
        }
        if (account.getAccountStatus().equals(AccountStatus.OPENING.type)) {
            return ResponseJsonResult.errorMsg("You have not completed the account opening process!");
        }
        return ResponseJsonResult.ok(true);

    }

    @Override
    public ResponseJsonResult queryAccountBlance(String userId) {

        //校验用户登录状态，查询Redis缓存
        String str = redisOperator.get(Constant.REDIS_USER_TOKEN + ":" + userId);
        if (str == null) {
            return ResponseJsonResult.build(LOGIN_EXPIRED_CODE,"Login expired, please login again !","");
        }
        Account account = getAccountInfo(userId);
        if (null == account) {
            return ResponseJsonResult.errorMsg("Account Anomaly！");
        }
        Double accountBlance = account.getAccountBlance() + account.getRewardAmount() + account.getMonthlyTicketBlance() +
                account.getUpgradeRewardBlance() + account.getCommissionBlance() + account.getRechargeRebateBlance()+account.getInvitationRewardBlance();
        return ResponseJsonResult.ok(accountBlance);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public ResponseJsonResult queryAccountInfo(String userId) {
        Account account = getAccountInfo(userId);
        if (null == account) {
            return ResponseJsonResult.errorMsg("Account Anomaly！");
        }
        return ResponseJsonResult.ok(account);
    }

    //获取账户信息
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Account getAccountInfo(String userId) {
        QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("account_leve", AccountLeve.ONE.id).eq("account_type", 0);
        return accountMapper.selectOne(queryWrapper);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean openAccount(String userId, long accountId) {
        return new LambdaUpdateChainWrapper<>(accountMapper)
                .eq(Account::getUserId, userId)
                .eq(Account::getAccountId, accountId).set(Account::getAccountStatus, AccountStatus.NORMAL.type)
                .set(Account::getUpdateTime, LocalDateTime.now()).update();
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public Double getAccountBlance(String userId) {
        QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("account_leve", AccountLeve.ONE.id).eq("account_type", 0);
        Account account = accountMapper.selectOne(queryWrapper);
        return account.getAccountBlance();
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public Double getSecondaryAccountBlance(String userId, Integer accountType) {
        QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("account_leve", AccountLeve.TWO.id).eq("account_type", accountType);
        Account account = accountMapper.selectOne(queryWrapper);
        return account.getAccountBlance();
    }

    //获取账户信息
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ResponseJsonResult querySecondaryAccountInfo(String userId) {
        QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("account_leve", AccountLeve.TWO.id);
        Account account = accountMapper.selectOne(queryWrapper);

        SecondaryAccountInfo secondaryAccountInfo = SecondaryAccountInfo.builder()
                .accountBlance(account.getAccountBlance())
                .accountType(account.getAccountType())
                .accountTypeName(account.getAccountTypeName())
                .build();
        return ResponseJsonResult.ok(secondaryAccountInfo);
    }



    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ResponseJsonResult totalInvitationGiftAndRebateReward(String userId) {
        if (StringUtil.isBlank(userId)) {
            return ResponseJsonResult.errorMsg("This userId cannot be empty !");
        }
        //校验用户登录状态，查询Redis缓存
        String str = redisOperator.get(Constant.REDIS_USER_TOKEN + ":" + userId);
        if (str == null) {
            return ResponseJsonResult.build(LOGIN_EXPIRED_CODE, "Login expired, please login again !", "");
        }

        Account account = getAccountInfo(userId);
        TotalInvitationGiftAndRebateRewardVO totalInvitationGiftAndRebateRewardVO = TotalInvitationGiftAndRebateRewardVO.builder()
                .totalInvitationGift(account.getInvitationRewardBlance())
                .totalRebateReward(account.getRechargeRebateBlance())
                .build();
        return ResponseJsonResult.ok(totalInvitationGiftAndRebateRewardVO);
    }


    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ResponseJsonResult totalInvitationRewardAndCommissionReward(String userId) {
        if (StringUtil.isBlank(userId)) {
            return ResponseJsonResult.errorMsg("This userId cannot be empty !");
        }
        //校验用户登录状态，查询Redis缓存
        String str = redisOperator.get(Constant.REDIS_USER_TOKEN + ":" + userId);
        if (str == null) {
            return ResponseJsonResult.build(LOGIN_EXPIRED_CODE, "Login expired, please login again !", "");
        }

        Account account = getAccountInfo(userId);
        TotalInvitationRewardAndCommissionRewardVO totalInvitationRewardAndCommissionRewardVO = TotalInvitationRewardAndCommissionRewardVO.builder()
                .totalInvitationReward(account.getInvitationRewardBlance())
                .totalCommissionReward(account.getCommissionBlance())
                .build();
        return ResponseJsonResult.ok(totalInvitationRewardAndCommissionRewardVO);
    }

}
