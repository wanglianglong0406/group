package com.hy.lottery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.additional.update.impl.LambdaUpdateChainWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hy.account.model.Account;
import com.hy.account.service.IAccountService;
import com.hy.constant.Constant;
import com.hy.enums.AccountStatus;
import com.hy.enums.OrderType;
import com.hy.enums.PayMethod;
import com.hy.enums.Platfrom;
import com.hy.lottery.mapper.BetRecordMapper;
import com.hy.lottery.model.BetRecord;
import com.hy.lottery.model.LotteryInfo;
import com.hy.lottery.model.bo.BetRecordBO;
import com.hy.lottery.service.IBetRecordService;
import com.hy.lottery.service.ILotteryInfoService;
import com.hy.order.model.bo.OrderBO;
import com.hy.order.service.IOrderService;
import com.hy.pojo.PagedGridResult;
import com.hy.pojo.ResponseJsonResult;
import com.hy.utils.RedisOperator;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.hy.constant.Constant.*;

/**
 * (ParityRecord)表服务实现类
 *
 * @author 寒夜
 * @since 2020-11-23 15:35:57
 */
@RestController
@Slf4j
public class BetRecordServiceImpl implements IBetRecordService {

    @Autowired
    private BetRecordMapper betRecordMapper;
    @Autowired
    private RedisOperator redisOperator;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private IOrderService iOrderService;
    @Autowired
    private ILotteryInfoService iLotteryInfoService;
    @Autowired
    private IAccountService iAccountService;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ResponseJsonResult queryBetRecordDetail(String userId, BetRecordBO betRecordBO) {

        if (StringUtil.isBlank(betRecordBO.getPeriod())) {
            return ResponseJsonResult.errorMsg("Period number cannot be empty！");
        }
        if (StringUtil.isBlank(betRecordBO.getType())) {
            return ResponseJsonResult.errorMsg("type cannot be empty！");
        }
        if (StringUtil.isBlank(userId)) {
            return ResponseJsonResult.errorMsg("userId cannot be empty！");
        }
        QueryWrapper<BetRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", betRecordBO.getType());
        queryWrapper.eq("period", betRecordBO.getPeriod());
        queryWrapper.eq("user_id", userId);
        if (StringUtil.isBlank(betRecordBO.getSelectColour())) {
            if (StringUtil.isBlank(betRecordBO.getNumber())) {
                return ResponseJsonResult.errorMsg("The selected number cannot be empty!");
            }
            queryWrapper.eq("number", betRecordBO.getNumber());
        }
        if (StringUtil.isBlank(betRecordBO.getNumber())) {
            if (StringUtil.isBlank(betRecordBO.getSelectColour())) {
                return ResponseJsonResult.errorMsg("The color added cannot be empty!");
            }
            queryWrapper.eq("select_colour", betRecordBO.getSelectColour());
        }
        BetRecord betRecord = betRecordMapper.selectOne(queryWrapper);
        return ResponseJsonResult.ok(betRecord);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ResponseJsonResult getBetRecordOrderToken(String userId) {
        String token = UUID.randomUUID().toString();
        redisOperator.set(REDIS_ORDER_TOKEN + ":" + userId, token);
        return ResponseJsonResult.ok(token);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResponseJsonResult placeBetRecordOrder(String userId, String orderToken, List<BetRecordBO> betRecordBOList) {

        String str = redisOperator.get(Constant.REDIS_USER_TOKEN + ":" + userId);
        if (str == null) {
            return ResponseJsonResult.build(LOGIN_EXPIRED_CODE,"Login expired, please login again !","");
        }
        ResponseJsonResult responseJsonResult;

        responseJsonResult = checkUserAccountInfoStatus(userId);

        if (responseJsonResult != null) return responseJsonResult;

        //校验订单token状态，查询Redis缓存
        String orderTokenKey = REDIS_ORDER_LOTTERY_TOKEN + ":" + userId;
        String lockKey = REDIS_LOCK_ORDER_BET_RECORD + ":" + userId;
        RLock rLock = redissonClient.getLock(lockKey);
        rLock.lock(5, TimeUnit.SECONDS);
        try {
            String order_token = redisOperator.get(orderTokenKey);
            log.info("******************************订单token为***********************{}", order_token);
            if (StringUtils.isBlank(order_token)) {
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


        String period_redis_str = redisOperator.get(LOTTERY_ACTION_NO_REDIS_LOCK);
        List<LotteryInfo> lotteryInfos = iLotteryInfoService.queryLotterInfoByPeriod(period_redis_str);
        String period = lotteryInfos.get(0).getPeriod();
        long lastOrderTime = lotteryInfos.get(0).getLastCreateOrderTime();
        Date date = new Date();
        long nowTime = date.getTime();
        //当前时间大于订单截至时间的时候，不允许投注订购（原因是订单截至时间之后需要走开奖的流程，所以不允许下单操作）
        if (nowTime > lastOrderTime) {
            return ResponseJsonResult.errorMsg("Order not allowed at current time !");
        }


        String type = "";
        for (BetRecordBO betRecordBO : Objects.requireNonNull(betRecordBOList)) {
            assert betRecordBO != null;
            ResponseJsonResult x = checkParams(userId, betRecordBO);

            if (x != null) return x;

            if (period.equals(betRecordBO.getPeriod())) {
                period = betRecordBO.getPeriod();
            }

            type = betRecordBO.getType();
            Long orderId = generateSystemOrderFlow(userId, betRecordBO.getBetAmount(),type,period);
            log.info("系统订单创建成，orderId = {} ", orderId);

            responseJsonResult = iLotteryInfoService.queryLotteryInfoList(type);
            if (responseJsonResult.getStatus() != 200) {
                return responseJsonResult;
            }

            ObjectMapper mapper = new ObjectMapper();
            LotteryInfo lotteryInfo = mapper.convertValue(responseJsonResult.getData(), LotteryInfo.class);

            int result = createRecord(userId, orderId, lotteryInfo.getPrice(), betRecordBO);
            if (result == 0) {
                return ResponseJsonResult.errorMsg("Database exception!");
            }


            responseJsonResult = iAccountService.accountPayment(userId, betRecordBO.getBetAmount(), orderId);
            if (responseJsonResult.getStatus() != 200) {
                return responseJsonResult;
            }

            responseJsonResult = updateBetRecordPayStatus(orderId, userId);
            if (responseJsonResult.getStatus() != 200) {
                return responseJsonResult;
            }

        }
        return ResponseJsonResult.ok(queryBetRecordInfos(period, type, 2));

    }

    //校验账户信息状态
    private ResponseJsonResult checkUserAccountInfoStatus(String userId) {
        ObjectMapper mapper = new ObjectMapper();
        ResponseJsonResult responseJsonResult;

        responseJsonResult = iAccountService.queryAccountInfo(userId);
        if (responseJsonResult.getStatus() != 200) {
            return responseJsonResult;
        }

        Account account = mapper.convertValue(responseJsonResult.getData(), Account.class);

        if (account.getAccountStatus().equals(AccountStatus.FROZEN.type)) {
            return ResponseJsonResult.errorMsg("The account has been frozen!");
        }
        if (account.getAccountStatus().equals(AccountStatus.CANCELLATION.type)) {
            return ResponseJsonResult.errorMsg("The account has been  cancelled!");
        }
        if (account.getAccountStatus().equals(AccountStatus.OPENING.type)) {
            return ResponseJsonResult.errorMsg("You have not completed the account opening process!");
        }

        Double  accountBlance = account.getAccountBlance();
        if (accountBlance < 10) {
            return ResponseJsonResult.errorMsg("Insufficient account balance");
        }
        return null;
    }

    //生成系统订单流水信息
    private Long  generateSystemOrderFlow(String userId, Double amount,String gameName,String period) {
        // 调用订单中心，创建系统订单流水信息
        //调用订单中心，创建系统订单
        OrderBO orderBO = OrderBO.builder()
                .userId(userId)
                .amount(amount)
                .discountRate(0)
                .orderType(OrderType.BETS.id)
                .platformId(Platfrom.Futures.id)
                .platformName(Platfrom.Futures.name)
                .gameName(gameName)
                .period(period)
                .payMethod(PayMethod.ACCOUNT_PAY.id)
                .realPayAmount(amount)
                .build();
        return iOrderService.createOrderInfo(orderBO);
    }

    //检查参数
    private ResponseJsonResult checkParams(String userId, BetRecordBO betRecordBO) {
        if (StringUtil.isBlank(betRecordBO.getType())) {
            return ResponseJsonResult.errorMsg("This type cannot be empty!");
        }
        if (StringUtil.isBlank(betRecordBO.getPeriod())) {
            return ResponseJsonResult.errorMsg("This period cannot be empty!");
        }
        if (StringUtil.isBlank(userId)) {
            return ResponseJsonResult.errorMsg("This userId cannot be empty!");
        }
        return null;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public int createRecord(String userId, Long orderId, Long price, BetRecordBO betRecordBO) {

        if (StringUtil.isNotBlank(betRecordBO.getSelectColour()) || StringUtil.isBlank(betRecordBO.getNumber())) {
            return betRecordMapper.insert(BetRecord.builder().betAmount(betRecordBO.getBetAmount())
                    .createTime(new Date())
                    .updateTime(new Date())
                    .isLuckFlag(1) // 是否幸运中奖 1：未中  2：中奖了
                    .openStatus(1) // 开奖状态 1：未开 2：已开
                    .payStatus(2)  // 支付状态 1：已支付  2：未支付
                    .orderId(orderId)//从订单中心获取
                    .period(betRecordBO.getPeriod())
                    .selectColour(betRecordBO.getSelectColour())
                    .type(betRecordBO.getType())
                    .odds(0.00)
                    .winOrLose(0.00)
                    .price(price)
                    .userId(userId).build());
        }

        if (StringUtil.isBlank(betRecordBO.getSelectColour()) || StringUtil.isNotBlank(betRecordBO.getNumber())) {
            return betRecordMapper.insert(BetRecord.builder().betAmount(betRecordBO.getBetAmount())
                    .createTime(new Date())
                    .updateTime(new Date())
                    .isLuckFlag(1)
                    .number(betRecordBO.getNumber())
                    .openStatus(1)
                    .orderId(orderId)//从订单中心获取
                    .period(betRecordBO.getPeriod())
                    .selectColour("")
                    .payStatus(1)
                    .type(betRecordBO.getType())
                    .odds(0.00)
                    .winOrLose(0.00)
                    .price(price)
                    .userId(userId).build());
        }
        return 0;

    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public BetRecord queryBetRecordInfoOne(String period, String type, String userId) {
        QueryWrapper<BetRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", type);
        queryWrapper.eq("open_status", 1);
        queryWrapper.eq("period", period);
        queryWrapper.eq("user_id", userId);
        BetRecord betRecord = betRecordMapper.selectOne(queryWrapper);
        return betRecord;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<BetRecord> queryBetRecordInfos(@RequestParam("period") String period, @RequestParam("type") String type, @RequestParam("payStatus") Integer payStatus) {
        QueryWrapper<BetRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", type);
        queryWrapper.eq("open_status", 1);
        queryWrapper.eq("pay_status", payStatus);
        queryWrapper.eq("period", period).orderByDesc("create_time");
        List<BetRecord> betRecords = betRecordMapper.selectList(queryWrapper);
        return betRecords;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean updateBetRecordInfo(Long orderId, String period, String type, String userId, Integer isluckFlag, String result, Double odds, Double winOrLose, Long price) {
        boolean update = new LambdaUpdateChainWrapper<BetRecord>(betRecordMapper)
                .eq(BetRecord::getPeriod, period)
                .eq(BetRecord::getUserId, userId)
                .eq(BetRecord::getOrderId, orderId)
                .eq(BetRecord::getType, type)
                .set(BetRecord::getIsLuckFlag, isluckFlag)
                .set(BetRecord::getOpenStatus, 2)
                .set(BetRecord::getResult, result)
                .set(BetRecord::getOdds, odds)
                .set(BetRecord::getWinOrLose, winOrLose)
                .set(BetRecord::getPrice, price)
                .set(BetRecord::getUpdateTime, new Date())
                .update();
        return update;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult queryBetRecordInfoList(String type, String userId, Integer pageNo, Integer pageSize, String startDate, String endDate) {

        if (StringUtil.isBlank(type)) {
            return PagedGridResult.builder().code(500).msg("type cannot be empty !").build();
        }

        String str = redisOperator.get(Constant.REDIS_USER_TOKEN + ":" + userId);
        if (str == null) {
            return PagedGridResult.builder().code(LOGIN_EXPIRED_CODE).msg("Login expired, please login again !").build();
        }

        QueryWrapper<BetRecord> queryWrapper = new QueryWrapper<BetRecord>();
        queryWrapper.eq("type", type);
        queryWrapper.eq("user_id", userId);
        queryWrapper.orderByDesc("create_time");
        if (StringUtil.isNotBlank(startDate)) {
            queryWrapper.ge("date_format(create_time,'%Y-%m-%d')", startDate);
        }
        if (StringUtil.isNotBlank(endDate)) {
            queryWrapper.le("date_format(create_time,'%Y-%m-%d')", endDate);
        }
        if (pageNo == null) {
            pageNo = PAGE_NO;
        }
        if (pageSize == null) {
            pageSize = PAGE_SIZE;
        }
        Page<BetRecord> page = new Page<BetRecord>(pageNo, pageSize, true);
        IPage<Map<String, Object>> iPage = betRecordMapper.selectMapsPage(page, queryWrapper);
        List<Map<String, Object>> betRecordList = iPage.getRecords();
        return PagedGridResult.builder().pageNo(pageNo).rows(betRecordList).pages(iPage.getPages()).total(iPage.getTotal()).build();

    }

//
//    //将支付订单号按照逗号切割，并且将其放入数组之中，然后添加到list集合之中
//    private List<String> splitOrderIds(String orderIds) {
//        String[] nums = orderIds.split(",");
//        List<String> orderId_list = new ArrayList<>();
//        Collections.addAll(orderId_list, nums);
//        return orderId_list;
//    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResponseJsonResult updateBetRecordPayStatus(Long orderId, String userId) {
        String period_redis_str = redisOperator.get(LOTTERY_ACTION_NO_REDIS_LOCK);
        List<LotteryInfo> lotteryInfos = iLotteryInfoService.queryLotterInfoByPeriod(period_redis_str);
        String period = lotteryInfos.get(0).getPeriod();
        long lastOrderTime = lotteryInfos.get(0).getLastCreateOrderTime();
        Date date = new Date();
        long nowTime = date.getTime();
        //当前时间大于订单截至时间的时候，不允许投注订购（原因是订单截至时间之后需要走开奖的流程，所以不允许下单操作）
        if (nowTime > lastOrderTime) {
            return ResponseJsonResult.errorMsg("Prize drawing is in progress. The current order is invalid. Payment is not allowed at this time !");
        }
        boolean result = new LambdaUpdateChainWrapper<BetRecord>(betRecordMapper)
                .eq(BetRecord::getOrderId, orderId)
                .eq(BetRecord::getUserId, userId)
                .eq(BetRecord::getPeriod, period)
                .eq(BetRecord::getOpenStatus, 1)
                .set(BetRecord::getPayStatus, 2)
                .set(BetRecord::getUpdateTime, new Date())
                .update();

        if (!result) {
            return ResponseJsonResult.errorMsg("数据异常!");
        } else {
            return ResponseJsonResult.build(200, "", true);
        }


    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ResponseJsonResult totalAmountOfBetting(String type, String userId, String startDate, String endDate) {
        if (StringUtil.isBlank(userId)) {
            return ResponseJsonResult.errorMsg("User ID cannot be empty ");
        }
        if (StringUtil.isBlank(type)) {
            return ResponseJsonResult.errorMsg("type cannot be empty ");
        }
        String str = redisOperator.get(Constant.REDIS_USER_TOKEN + ":" + userId);
        if (str == null) {
            return ResponseJsonResult.build(LOGIN_EXPIRED_CODE,"Login expired, please login again !","");
        }
        QueryWrapper<BetRecord> queryWrapper = new QueryWrapper<BetRecord>();
        queryWrapper.eq("type", type);
        queryWrapper.eq("user_id", userId);
        if (StringUtil.isNotBlank(startDate)) {
            queryWrapper.ge("date_format(create_time,'%Y-%m-%d')", startDate);
        }
        if (StringUtil.isNotBlank(endDate)) {
            queryWrapper.le("date_format(create_time,'%Y-%m-%d')", endDate);
        }
        List<BetRecord> betRecords = betRecordMapper.selectList(queryWrapper);
        double totalAmount = 0.00;
        for (BetRecord betRecord : betRecords) {
            totalAmount = totalAmount + betRecord.getBetAmount();
        }
        return ResponseJsonResult.ok(totalAmount);
    }


    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ResponseJsonResult statisticsOfBetLoss(String type, String userId, String startDate, String endDate) {
        if (StringUtil.isBlank(userId)) {
            return ResponseJsonResult.errorMsg("User ID cannot be empty ");
        }
        if (StringUtil.isBlank(type)) {
            return ResponseJsonResult.errorMsg("type cannot be empty ");
        }
        String str = redisOperator.get(Constant.REDIS_USER_TOKEN + ":" + userId);
        if (str == null) {
            return ResponseJsonResult.build(LOGIN_EXPIRED_CODE,"Login expired, please login again !","");
        }
        QueryWrapper<BetRecord> queryWrapper = new QueryWrapper<BetRecord>();
        queryWrapper.eq("type", type);
        queryWrapper.eq("user_id", userId);
        if (StringUtil.isNotBlank(startDate)) {
            queryWrapper.ge("date_format(create_time,'%Y-%m-%d')", startDate);
        }
        if (StringUtil.isNotBlank(endDate)) {
            queryWrapper.le("date_format(create_time,'%Y-%m-%d')", endDate);
        }


        List<BetRecord> betRecords = betRecordMapper.selectList(queryWrapper);
        double lossAmount = 0.00;
        for (BetRecord betRecord : betRecords) {
            if (betRecord.getIsLuckFlag() == 1) {
                lossAmount = lossAmount + betRecord.getBetAmount();
            }
        }
        return ResponseJsonResult.ok(lossAmount);
    }

}