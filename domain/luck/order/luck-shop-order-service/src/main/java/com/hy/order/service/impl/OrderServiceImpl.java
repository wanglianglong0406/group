package com.hy.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.additional.update.impl.LambdaUpdateChainWrapper;
import com.google.gson.Gson;
import com.hy.center.model.VipInfo;
import com.hy.center.model.bo.ItemsBiz;
import com.hy.center.service.ICenterStageService;
import com.hy.constant.Constant;
import com.hy.enums.OrderType;
import com.hy.enums.PayMethod;
import com.hy.order.mapper.OrderMapper;
import com.hy.order.model.Orders;
import com.hy.order.model.bo.OrderBO;
import com.hy.order.model.vo.OrderTaskVO;
import com.hy.order.model.vo.OrdersVO;
import com.hy.order.service.IOrderService;
import com.hy.pojo.PagedGridResult;
import com.hy.pojo.ResponseJsonResult;
import com.hy.user.model.User;
import com.hy.user.service.IUserService;
import com.hy.utils.JsonUtils;
import com.hy.utils.RedisOperator;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.hy.constant.Constant.*;


/**
 * @Description: $- OrderServiceImpl 订单中心相关业务实现类-$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/12/3 18:45
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/12/3 18:45
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@RestController
@Slf4j
public class OrderServiceImpl implements IOrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private RedisOperator redisOperator;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private ICenterStageService iCenterStageService;
    @Autowired
    private IUserService iUserService;

    Gson gson = new Gson();

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ResponseJsonResult getOrderToken(String userId) {
        ResponseJsonResult x = checkParams(userId, "1", 1, 1);
        if (x != null) return x;
        String token = UUID.randomUUID().toString();
        redisOperator.set(REDIS_ORDER_TOKEN + ":" + userId, token);
        return ResponseJsonResult.ok(token);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResponseJsonResult orderGrabbingTask(String userId, String orderToken) {

        ResponseJsonResult x = checkParams(userId, orderToken, 1, 1);
        if (x != null) return x;

        //校验订单token状态，查询Redis缓存
        String orderTokenKey = REDIS_ORDER_TOKEN + ":" + userId;
        String lockKey = REDIS_LOCK_ORDER_TASK_RECORD + ":" + userId;
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

        User user = iUserService.queryUserInfoByUserId(userId);

        VipInfo vipInfo = iCenterStageService.getVipInfosByVipLevel(user.getMembershipLevel());
        Double commissionRate = vipInfo.getCommissionRate();
        Integer numberOfTasks = vipInfo.getNumberOfTasks();
        List<ItemsBiz> itemsBizList = iCenterStageService.getItemsList(userId, numberOfTasks);

        assert itemsBizList != null;

        int index = (int) (Math.random() * itemsBizList.size());
        ItemsBiz items = itemsBizList.get(index + 1);
        Double commissionRewarsAmount = commissionRate * items.getPrice() / 100;
        Long orderId = generateSystemOrderFlow(items.getUrl(), userId, items.getPrice(), commissionRate, commissionRewarsAmount, PayMethod.SYSTEM_PAY.id, OrderType.TASK_COMMISSION.id);

        log.info("==============================订单号为：======" + orderId);
        log.info("==============================转为String 订单号为：======" + String.valueOf(orderId));

        //完成后需要移除当前商品
        itemsBizList.remove(index);
        //剩余订单任务数量
        Integer numberOfRemainingTasks = itemsBizList.size();
        //已完成订单任务数量
        Integer numberOfTasksCompleted = numberOfTasks - numberOfRemainingTasks;
        OrderTaskVO orderTaskVO = OrderTaskVO.builder()
                .userId(userId)
                .orderId(String.valueOf(orderId))
                .itemsId(items.getId())
                .itemName(items.getItemName())
                .url(items.getUrl())
                .content(items.getContent())
                .price(items.getPrice())
                .commissionRate(commissionRate)
                .commissionRewarsAmount(commissionRewarsAmount)
                .numberOfTasks(numberOfTasks)
                .numberOfRemainingTasks(numberOfRemainingTasks)
                .numberOfTasksCompleted(numberOfTasksCompleted)
                .build();

        String itemsListRedisKey = ITEMS_LIST + ":" + userId;
        redisOperator.set(itemsListRedisKey, JsonUtils.objectToJson(itemsBizList));
        return ResponseJsonResult.ok(orderTaskVO);
    }


    //生成系统订单流水
    private Long generateSystemOrderFlow(String imgUrl, String userId, Double amount, Double commissionRate, Double commissionRewarsAmount, Integer payMethod, Integer orderType) {
        // 调用订单中心，创建系统订单流水信息
        OrderBO orderBO = OrderBO.builder()
                .userId(userId)
                .amount(amount)
                .realPayAmount(amount)
                .commissionRate(commissionRate)
                .commissionRewarsAmount(commissionRewarsAmount)
                .imgUrl(imgUrl)
                .discountRate(0)
                .orderType(orderType)
                .payMethod(payMethod)
                .build();
        return createOrderInfo(orderBO);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Long createOrderInfo(OrderBO orderBO) {
        Date date = new Date();
        Orders orders = Orders.builder()
                .userId(orderBO.getUserId())
                .userName(orderBO.getUserName())
                .bankName(orderBO.getBankName())
                .amount(orderBO.getAmount())
                .commissionRate(orderBO.getCommissionRate())
                .commissionRewarsAmount(orderBO.getCommissionRewarsAmount())
                .commissionRefundAmount(orderBO.getCommissionRefundAmount())
                .orderStatus(10) //订单状态;10：待付款  20：已付款   30  交易成功  40：交易关闭（待付款时，用户取消 或 长时间未付款，系统识别后自动关闭）
                .imgUrl(orderBO.getImgUrl())
                .orderType(orderBO.getOrderType())
                .orderStatusName("create order success")
                .payMethod(orderBO.getPayMethod())
                .realPayAmount(orderBO.getRealPayAmount())
                .isDelete(0)
                .discountRate(orderBO.getDiscountRate())
                .extand("create order success")
                .createTime(date)
                .updateTime(date)
                .closeTime(new Date())
                .successTime(new Date())
                .payTime(new Date())
                .build();

        orderMapper.insert(orders);
        Long orderId = orders.getOrderId();
        System.out.println("订单号为：" + orderId);
        return orderId;
    }


    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean updateOrderInfo(Long orderId, Integer orderStatus, String orderStatusName, String extand) {
        return new LambdaUpdateChainWrapper<>(orderMapper)
                .eq(Orders::getOrderId, orderId)
                .set(Orders::getOrderStatus, orderStatus)
                .set(Orders::getOrderStatusName, orderStatusName)
                .set(Orders::getExtand, extand)
                .set(Orders::getUpdateTime, new Date())
                .set(Orders::getSuccessTime, new Date())
                .set(Orders::getCloseTime, new Date())
                .set(Orders::getPayTime, new Date()).update();
    }

    @Override
    public ResponseJsonResult queryTaskOrderRecordList(String userId, Integer orderStauts, Integer pageNo, Integer pageSize, String startDate, String endDate) {

        ResponseJsonResult x = checkParams(userId, "1", 1, orderStauts);
        if (x != null) return x;
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("user_id", userId).eq("order_type", OrderType.TASK_COMMISSION.id)
                .eq("order_status", orderStauts);
        return getPagedGridResult(pageNo, pageSize, startDate, endDate, queryWrapper);
    }


    @Override
    public ResponseJsonResult queryRechargeAndWithdrawOrderRecordList(String userId, Integer orderType, Integer pageNo, Integer pageSize, String startDate, String endDate) {

        ResponseJsonResult x = checkParams(userId, "1", orderType, 1);
        if (x != null) return x;
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("user_id", userId).eq("order_type", orderType).orderByDesc("create_time");

        return getPagedGridResult(pageNo, pageSize, startDate, endDate, queryWrapper);
    }


    private ResponseJsonResult getPagedGridResult(Integer pageNo, Integer pageSize, String startDate, String endDate, QueryWrapper<Orders> queryWrapper) {
        if (StringUtil.isNotBlank(startDate)) {
            queryWrapper.ge("date_format(create_time,'%Y-%m-%d')", startDate);
        }
        if (StringUtil.isNotBlank(endDate)) {

            queryWrapper.le("date_format(create_time,'%Y-%m-%d')", endDate);
        }

        if(StringUtil.isBlank(startDate)&& StringUtil.isBlank(endDate)){
            String nowDate = LocalDate.now().toString();
            queryWrapper.eq("date_format(create_time,'%Y-%m-%d')", nowDate);
        }

        if (pageNo == null) {
            pageNo = PAGE_NO;
        }
        if (pageSize == null) {
            pageSize = PAGE_SIZE;
        }

        Page<Orders> page = new Page<>(pageNo, pageSize, true);
        IPage<Orders> iPage = orderMapper.selectPage(page, queryWrapper);
        List<Orders> orderList = iPage.getRecords();

        List<OrdersVO> ordersVOList = new ArrayList<>();
        orderList.forEach(ol -> {
            OrdersVO ordersVO = OrdersVO.builder()
                    .amount(ol.getAmount())
                    .bankName(ol.getBankName())
                    .closeTime(ol.getCloseTime())
                    .commissionRate(ol.getCommissionRate())
                    .commissionRefundAmount(ol.getCommissionRefundAmount())
                    .orderId(String.valueOf(ol.getOrderId()))
                    .imgUrl(ol.getImgUrl())
                    .commissionRewarsAmount(ol.getCommissionRewarsAmount())
                    .createTime(ol.getCreateTime())
                    .discountRate(ol.getDiscountRate())
                    .extand(ol.getExtand())
                    .isDelete(ol.getIsDelete())
                    .isRechargeFlag(ol.getIsRechargeFlag())
                    .orderStatus(ol.getOrderStatus())
                    .orderStatusName(ol.getOrderStatusName())
                    .orderType(ol.getOrderType())
                    .payMethod(ol.getPayMethod())
                    .payTime(ol.getPayTime())
                    .realPayAmount(ol.getRealPayAmount())
                    .successTime(ol.getSuccessTime())
                    .updateTime(ol.getUpdateTime())
                    .userId(ol.getUserId())
                    .userName(ol.getUserName())
                    .build();
            ordersVOList.add(ordersVO);
        });

        return ResponseJsonResult.ok(PagedGridResult.builder().pageNo(pageNo).rows(ordersVOList).pages(iPage.getPages()).total(iPage.getTotal()).build());
    }

    private ResponseJsonResult checkParams(String userId, String orderToken, Integer orderType, Integer orderStauts) {
        if (StringUtil.isBlank(userId)) {
            return ResponseJsonResult.errorMsg("This userId cannot be empty !");
        }
        //校验用户登录状态，查询Redis缓存
        String str = redisOperator.get(Constant.REDIS_USER_TOKEN + ":" + userId);
        if (str == null) {
            return ResponseJsonResult.build(LOGIN_EXPIRED_CODE, "Login expired, please login again !", "");
        }

        if (StringUtil.isBlank(orderToken)) {
            return ResponseJsonResult.errorMsg("This orderToken cannot be empty !");
        }

        if (orderType == null) {
            return ResponseJsonResult.errorMsg("This orderType cannot be empty !");
        }

        if (orderStauts == null) {
            return ResponseJsonResult.errorMsg("This orderStauts cannot be empty !");
        }
        return null;
    }

    @Override
    public Orders getUserFirstRechargeInfo(String userId) {
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("order_type", OrderType.DEPOSIT.id).eq("is_recharge_flag", 1);
        return orderMapper.selectOne(queryWrapper);
    }


    @Override
    public ResponseJsonResult getOrderInfoPagedGridResult(Integer orderType, Long orderId, String[] userIds, Integer pageNo, Integer pageSize, String startDate, String endDate) {
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
        if (StringUtil.isNotBlank(startDate)) {
            queryWrapper.ge("date_format(create_time,'%Y-%m-%d')", startDate);
        }
        if (StringUtil.isNotBlank(endDate)) {
            queryWrapper.le("date_format(create_time,'%Y-%m-%d')", endDate);
        }
        if (orderType != null) {
            queryWrapper.eq("order_type", orderType);
        }
        if (orderId != null) {
            queryWrapper.eq("order_d", orderId);
        }
        if (orderType != null) {
            queryWrapper.in("user_id", Arrays.asList(userIds));
        }
        if (pageNo == null) {
            pageNo = PAGE_NO;
        }
        if (pageSize == null) {
            pageSize = PAGE_SIZE;
        }

        Page<Orders> page = new Page<>(pageNo, pageSize, true);
        IPage<Orders> iPage = orderMapper.selectPage(page, queryWrapper);
        List<Orders> orderList = iPage.getRecords();

        List<OrdersVO> ordersVOList = new ArrayList<>();
        orderList.forEach(ol -> {
            OrdersVO ordersVO = OrdersVO.builder()
                    .amount(ol.getAmount())
                    .bankName(ol.getBankName())
                    .closeTime(ol.getCloseTime())
                    .commissionRate(ol.getCommissionRate())
                    .commissionRefundAmount(ol.getCommissionRefundAmount())
                    .orderId(String.valueOf(ol.getOrderId()))
                    .imgUrl(ol.getImgUrl())
                    .commissionRewarsAmount(ol.getCommissionRewarsAmount())
                    .createTime(ol.getCreateTime())
                    .discountRate(ol.getDiscountRate())
                    .extand(ol.getExtand())
                    .isDelete(ol.getIsDelete())
                    .isRechargeFlag(ol.getIsRechargeFlag())
                    .orderStatus(ol.getOrderStatus())
                    .orderStatusName(ol.getOrderStatusName())
                    .orderType(ol.getOrderType())
                    .payMethod(ol.getPayMethod())
                    .payTime(ol.getPayTime())
                    .realPayAmount(ol.getRealPayAmount())
                    .successTime(ol.getSuccessTime())
                    .updateTime(ol.getUpdateTime())
                    .userId(ol.getUserId())
                    .userName(ol.getUserName())
                    .build();
            ordersVOList.add(ordersVO);
        });
        return ResponseJsonResult.ok(PagedGridResult.builder().pageNo(pageNo).rows(ordersVOList).pages(iPage.getPages()).total(iPage.getTotal()).build());


    }

    @Override
    public List<Orders> getFinancialOrderInfoList() {
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_type", OrderType.DAILY_INTEREST.id);
        return orderMapper.selectList(queryWrapper);

    }
}
