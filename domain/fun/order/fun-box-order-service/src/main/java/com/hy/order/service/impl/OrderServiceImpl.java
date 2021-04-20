package com.hy.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.additional.update.impl.LambdaUpdateChainWrapper;
import com.hy.constant.Constant;
import com.hy.enums.OrderType;
import com.hy.order.mapper.OrderMapper;
import com.hy.order.model.Orders;
import com.hy.order.model.bo.*;
import com.hy.order.model.vo.*;
import com.hy.order.service.IOrderService;
import com.hy.pojo.PagedGridResult;
import com.hy.pojo.ResponseJsonResult;
import com.hy.search.model.PayMethod;
import com.hy.search.service.IPayMethodService;
import com.hy.utils.RedisOperator;
import jodd.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
public class OrderServiceImpl implements IOrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private RedisOperator redisOperator;
    @Autowired
    private IPayMethodService iPayMethodService;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResponseJsonResult queryOrderInfo() {
        return null;
    }


    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Double totalCashFlow(String userId) {
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<Orders>();
        queryWrapper.select("sum(real_pay_amount) amount")
                .eq("order_type", 1)
                .and(u -> u.eq("user_id", userId))
                .and(os -> os.eq("order_status", 30));
        Orders orders = orderMapper.selectOne(queryWrapper);
        if (orders == null) {
            return 0.00;
        }
        return orders.getAmount();
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ResponseJsonResult totalFundsStatisticsFlow(String userId, Integer platformId, String startDate, String endDate) {

        if (platformId == null) {
            return ResponseJsonResult.errorMsg("platformId cannot be empty!");
        }
        if (StringUtil.isBlank(userId)) {
            return ResponseJsonResult.errorMsg("User ID cannot be empty!");
        }

        //校验用户登录状态，查询Redis缓存
        String str = redisOperator.get(Constant.REDIS_USER_TOKEN + ":" + userId);
        if (str == null) {
            return ResponseJsonResult.build(LOGIN_EXPIRED_CODE,"Login expired, please login again !","");
        }
        double totalBets = getTotalBets(userId, platformId, startDate, endDate);
        double totalWinsLosses = getTotalWinsLosses(userId, platformId, startDate, endDate);
        TotalBetVO totalBetVO = TotalBetVO.builder().totalBets(totalBets).totalWinsLosses(totalWinsLosses).build();
        return ResponseJsonResult.ok(totalBetVO);
    }

    //统计总投注
    private double getTotalBets(String userId, Integer platformId, String startDate, String endDate) {
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<Orders>();
        if (StringUtil.isNotBlank(startDate)) {
            queryWrapper.ge("date_format(create_time,'%Y-%m-%d')", startDate);
        }
        if (StringUtil.isNotBlank(endDate)) {
            queryWrapper.le("date_format(create_time,'%Y-%m-%d')", endDate);
        }
        queryWrapper.select("sum(real_pay_amount) amount")
                .eq("order_type", 1)
                .eq("platform_id", platformId)
                .and(u -> u.eq("user_id", userId))
                .and(os -> os.eq("order_status", 30));
        Orders orders = orderMapper.selectOne(queryWrapper);
        double totalBets = orders.getAmount();
        return totalBets;
    }

    //统计总输赢
    private double getTotalWinsLosses(String userId, Integer platformId, String startDate, String endDate) {
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<Orders>();
        if (StringUtil.isNotBlank(startDate)) {
            queryWrapper.ge("date_format(create_time,'%Y-%m-%d')", startDate);
        }
        if (StringUtil.isNotBlank(endDate)) {
            queryWrapper.le("date_format(create_time,'%Y-%m-%d')", endDate);
        }
        queryWrapper.select("sum(real_pay_amount) amount")
                .eq("order_type", 1)
                .eq("is_luck", 1)
                .eq("platform_id", platformId)
                .and(u -> u.eq("user_id", userId))
                .and(os -> os.eq("order_status", 30));
        Orders orders = orderMapper.selectOne(queryWrapper);
        double totalWinsLosses = orders.getAmount();
        return totalWinsLosses;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Double totalRechargeFlow(String userId) {
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<Orders>();
        queryWrapper.select("sum(real_pay_amount) amount")
                .eq("order_type", 2)
                .and(u -> u.eq("user_id", userId))
                .and(os -> os.eq("order_status", 30));
        Orders orders = orderMapper.selectOne(queryWrapper);
        if (orders == null) {
            return 0.00;
        }
        return orders.getAmount();
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Double totalAdateFlow(String userId, String startDate, String endDate) {
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<Orders>();
        queryWrapper.select("sum(real_pay_amount) amount")
                .in("order_type", Arrays.asList(1, 2))
                .and(u -> u.eq("user_id", userId))
                .and(os -> os.eq("order_status", 30))
                .and(sd -> sd.ge("date_format(create_time,'%Y-%m-%d')", startDate))
                .and(ed -> ed.le("date_format(create_time,'%Y-%m-%d')", endDate));

        queryWrapper.ge("date_format(create_time,'%Y-%m-%d')", startDate);
        Orders orders = orderMapper.selectOne(queryWrapper);
        if (orders == null) {
            return 0.00;
        }
        return orders.getAmount();
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<Orders> totalThisMonthRechargeFlow(String userId, String month) {

        QueryWrapper<Orders> queryWrapper = new QueryWrapper<Orders>();
        queryWrapper.eq("user_id", userId).eq("order_type", 2).eq("order_status", 30)
                .likeRight("date_format(create_time,'%Y-%m')", month);
        List<Orders> orders = orderMapper.selectList(queryWrapper);
        return orders;
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
                .orderStatus(10) //订单状态;10：待付款  20：已付款   30  交易成功  40：交易关闭（待付款时，用户取消 或 长时间未付款，系统识别后自动关闭）
                .orderType(orderBO.getOrderType())
                .orderStatusName("create order success")
                .platformId(orderBO.getPlatformId())
                .platformName(orderBO.getPlatformName())
                .gameName(orderBO.getGameName())
                .period(orderBO.getPeriod())
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

        if (orderBO.getOrderType().equals(OrderType.DEPOSIT.id)) {
            List<Orders> ordersList = queryOrdersInfoByUserIdAndOrderType(orderBO.getUserId(), orderBO.getOrderType());
            if (ordersList.size() == 0) {
                //第一笔存款
                orders.setIsRechargeFlag(1);
            }else if(ordersList.size() == 1){
                //第二笔存款
                orders.setIsRechargeFlag(2);
            } else {
                orders.setIsRechargeFlag(3);
            }
        }
        orderMapper.insert(orders);

        return orders.getOrderId();
    }


    public List<Orders> queryOrdersInfoByUserIdAndOrderType(String userId, Integer orderType) {
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<Orders>();
        queryWrapper.eq("user_id", userId).eq("order_type", 2).eq("order_status", 30);
        List<Orders> orders = orderMapper.selectList(queryWrapper);
        return orders;
    }

    //统计用户当天充值金额
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Double totalRechargeAmount(String userId) {
        Orders orders = queryfirstRechargeOrder(userId);
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<Orders>();
        queryWrapper.select("sum(real_pay_amount) amount")
                .eq("order_type", 2).eq("user_id", userId).eq("order_status", 30)
                .eq("date_format(create_time,'%Y-%m-%d')", orders.getCreateTime());
        orders = orderMapper.selectOne(queryWrapper);
        if (orders == null) {
            return 0.00;
        }
        return orders.getAmount();
    }

    //查询用户第一笔充值订单信息
    private Orders queryfirstRechargeOrder(String userId) {
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<Orders>();
        queryWrapper.eq("user_id", userId).eq("order_type", 2).eq("order_status", 30).eq("is_recharge_flag", 1);
        Orders orders = orderMapper.selectOne(queryWrapper);
        return orders;
    }
    //查询用户第二笔充值订单信息
    private Orders querySecondRechargeOrder(String userId) {
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<Orders>();
        queryWrapper.eq("user_id", userId).eq("order_type", 2).eq("order_status", 30).eq("is_recharge_flag", 2);
        Orders orders = orderMapper.selectOne(queryWrapper);
        return orders;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean updateOrderInfo(Long orderId, Integer orderStatus, String orderStatusName,String extand) {
        boolean result = new LambdaUpdateChainWrapper<Orders>(orderMapper)
                .eq(Orders::getOrderId, orderId)
                .set(Orders::getOrderStatus, orderStatus)
                .set(Orders::getOrderStatusName,orderStatusName)
                .set(Orders::getExtand, extand)
                .set(Orders::getUpdateTime, new Date())
                .set(Orders::getSuccessTime, new Date())
                .set(Orders::getCloseTime, new Date())
                .set(Orders::getPayTime, new Date()).update();
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean updateBetOrderInfoWinOrLosses(Long orderId, Integer isLuck, Double winOrLosse) {
        return new LambdaUpdateChainWrapper<Orders>(orderMapper)
                .eq(Orders::getOrderId, orderId)
                .set(Orders::getIsLuck, isLuck)
                .set(Orders::getWinOrLosse, winOrLosse)
                .set(Orders::getUpdateTime, new Date())
                .update();
    }


    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult queryBetOrderInfoList(String userId, Integer platformId, Integer pageNo, Integer pageSize, String startDate, String endDate) {
        if (StringUtil.isBlank(userId)) {
            return PagedGridResult.builder().code(500).msg("This userId cannot be empty !").build();
        }
        //校验用户登录状态，查询Redis缓存
        String str = redisOperator.get(Constant.REDIS_USER_TOKEN + ":" + userId);
        if (str == null) {
            return PagedGridResult.builder().code(LOGIN_EXPIRED_CODE).msg("Login expired, please login again !").build();
        }
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<Orders>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.orderByDesc("create_time");
        if (StringUtil.isNotBlank(startDate)) {
            queryWrapper.ge("date_format(create_time,'%Y-%m-%d')", startDate);
        }
        if (StringUtil.isNotBlank(endDate)) {
            queryWrapper.le("date_format(create_time,'%Y-%m-%d')", endDate);
        }
        if (platformId != null) {
            queryWrapper.eq("platform_id", platformId);
        } else {
            queryWrapper.in("platform_id", Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12));
        }
        if (pageNo == null) {
            pageNo = PAGE_NO;
        }
        if (pageSize == null) {
            pageSize = PAGE_SIZE;
        }

        Page<Orders> page = new Page<Orders>(pageNo, pageSize, true);
        IPage<Orders> iPage = orderMapper.selectPage(page, queryWrapper);
        List<Orders> ordersList = iPage.getRecords();

        List<BettingOrderRecordVO> bettingOrderRecordVOList = new ArrayList<>();
        ordersList.forEach(orders -> {
            BettingOrderRecordVO bettingOrderRecordVO = BettingOrderRecordVO.builder()
                    .orderId(orders.getOrderId())
                    .gameName(orders.getGameName())
                    .period(orders.getPeriod())
                    .isLuck(orders.getIsLuck())
                    .winOrLosse(orders.getWinOrLosse())
                    .realPayAmount(orders.getRealPayAmount())
                    .platformName(orders.getPlatformName())
                    .time(orders.getCloseTime())
                    .build();
            bettingOrderRecordVOList.add(bettingOrderRecordVO);
        });

        return PagedGridResult.builder().pageNo(pageNo).rows(bettingOrderRecordVOList).pages(iPage.getPages()).total(iPage.getTotal()).build();

    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult queryGeneralRecordInfoList(String userId, Integer orderType, Integer pageNo, Integer pageSize, String startDate, String endDate) {

        if (StringUtil.isBlank(userId)) {
            return PagedGridResult.builder().code(500).msg("This userId cannot be empty !").build();
        }
        //校验用户登录状态，查询Redis缓存
        String str = redisOperator.get(Constant.REDIS_USER_TOKEN + ":" + userId);
        if (str == null) {
            return PagedGridResult.builder().code(LOGIN_EXPIRED_CODE).msg("Login expired, please login again !").build();
        }
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<Orders>();

        if (orderType != null) {
            queryWrapper.eq("order_type", orderType);
        }
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

        Page<Orders> page = new Page<Orders>(pageNo, pageSize, true);
        IPage<Orders> iPage = orderMapper.selectPage(page, queryWrapper);
        List<Orders> ordersList = iPage.getRecords();

        List<GeneralRecordVO> generalRecordVOList = new ArrayList<>();
        ordersList.forEach(orders -> {

            List<PayMethod> payMethodList = iPayMethodService.queryPayMethodInfoList();
            for (PayMethod payMethod : payMethodList) {
                if (orders.getPayMethod().equals(payMethod.getId())) {
                    GeneralRecordVO generalRecordVO = GeneralRecordVO.builder()
                            .amount(orders.getAmount())
                            .orderId(orders.getOrderId())
                            .orderStatusName(orders.getOrderStatusName())
                            .payMethodName(payMethod.getPayMethodName())
                            .detail(orders.getExtand())
                            .title(payMethod.getTitle())
                            .receivedAmount(orders.getRealPayAmount())
                            .createTime(orders.getCreateTime())
                            .build();
                    generalRecordVOList.add(generalRecordVO);
                }

            }


        });

        return PagedGridResult.builder().pageNo(pageNo).rows(generalRecordVOList).pages(iPage.getPages()).total(iPage.getTotal()).build();

    }
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ResponseJsonResult totalTransfer(String userId) {

        if (StringUtil.isBlank(userId)) {
            return ResponseJsonResult.errorMsg("User ID cannot be empty!");
        }

        //校验用户登录状态，查询Redis缓存
        String str = redisOperator.get(Constant.REDIS_USER_TOKEN + ":" + userId);
        if (str == null) {
            return ResponseJsonResult.build(LOGIN_EXPIRED_CODE,"Login expired, please login again !","");
        }
        TotalTransferVO totalTransferVO = TotalTransferVO.builder()
                .totalTransferInAmount(totalTransferIn(userId))
                .totalTransferOutAmount(totalTransferOut(userId))
                .build();
        return ResponseJsonResult.ok(totalTransferVO);
    }

    public Double totalTransferIn(String userId) {
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<Orders>();
        queryWrapper.select("sum(real_pay_amount) amount")
                .eq("order_type", OrderType.TRANSFER_IN.id)
                .eq("user_id", userId).eq("order_status", 30);
        Orders orders = orderMapper.selectOne(queryWrapper);
        if (orders == null) {
            return 0.00;
        }
        return orders.getAmount();
    }

    public Double totalTransferOut(String userId) {
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<Orders>();
        queryWrapper.select("sum(real_pay_amount) amount")
                .eq("order_type", OrderType.TRANSFER_OUT.id)
                .eq("user_id", userId).eq("order_status", 30);
        Orders orders = orderMapper.selectOne(queryWrapper);
        if (orders == null) {
            return 0.00;
        }
        return orders.getAmount();
    }


    //充值和提现订单信息历史记录
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult queryDepositAndWithdrawInfoList(String userId, String userName, Integer pageNo, Integer pageSize, String startDate, String endDate) {

        if (StringUtil.isBlank(userId)) {
            return PagedGridResult.builder().code(500).msg("This userId cannot be empty !").build();
        }
        //校验用户登录状态，查询Redis缓存
        String str = redisOperator.get(Constant.REDIS_USER_TOKEN + ":" + userId);
        if (str == null) {
            return PagedGridResult.builder().code(LOGIN_EXPIRED_CODE).msg("Login expired, please login again !").build();
        }
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<Orders>();

        if (userName != null) {
            queryWrapper.like("user_name", userName);
        }
        queryWrapper.eq("user_id", userId);
        queryWrapper.in("order_type", Arrays.asList(2,3));

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

        Page<Orders> page = new Page<Orders>(pageNo, pageSize, true);
        IPage<Orders> iPage = orderMapper.selectPage(page, queryWrapper);
        List<Orders> ordersList = iPage.getRecords();

        List<DepositAndWithdrawVO> depositAndWithdrawVOList = new ArrayList<>();
        ordersList.forEach(orders -> {
            List<PayMethod> payMethodList = iPayMethodService.queryPayMethodInfoList();
            DepositAndWithdrawVO depositAndWithdrawVO = null;
            for (PayMethod payMethod : payMethodList) {
               depositAndWithdrawVO = DepositAndWithdrawVO.builder()
                        .bankName(orders.getBankName())
                        .userName(orders.getUserName())
                        .orderId(orders.getOrderId())
                        .orderStatusName(orders.getOrderStatusName())
                        .payMethodName(payMethod.getPayMethodName())
                        .receivedAmount(orders.getRealPayAmount())
                        .amount(orders.getAmount())
                        .build();

            }
            depositAndWithdrawVOList.add(depositAndWithdrawVO);

        });
        return PagedGridResult.builder().pageNo(pageNo).rows(depositAndWithdrawVOList).pages(iPage.getPages()).total(iPage.getTotal()).build();
    }


    //统计存款总额和取款总额
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ResponseJsonResult totalDepositAndWithdraw(String userId) {

        if (StringUtil.isBlank(userId)) {
            return ResponseJsonResult.errorMsg("This userId cannot be empty !");
        }
        //校验用户登录状态，查询Redis缓存
        String str = redisOperator.get(Constant.REDIS_USER_TOKEN + ":" + userId);
        if (str == null) {
            return ResponseJsonResult.build(LOGIN_EXPIRED_CODE, "Login expired, please login again !", "");
        }

        TotalDepositAndWithdrawVO totalDepositAndWithdrawVO = TotalDepositAndWithdrawVO.builder()
                .totalDeposit(totalDeposit(userId))
                .totalWithdraw(totalWithdraw(userId))
                .build();
        return ResponseJsonResult.ok(totalDepositAndWithdrawVO);
    }

    public Double totalDeposit(String userId){
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<Orders>();
        queryWrapper.select("sum(amount) amount")
                .eq("order_type", OrderType.DEPOSIT.id)
                .eq("user_id", userId).eq("order_status", 30);
        Orders orders = orderMapper.selectOne(queryWrapper);
        if (orders == null) {
            return 0.00;
        }
        return orders.getAmount();
    }

    public Double totalWithdraw(String userId){
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<Orders>();
        queryWrapper.select("sum(amount) amount")
                .eq("order_type", OrderType.WITHDRAW.id)
                .eq("user_id", userId).eq("order_status", 30);
        Orders orders = orderMapper.selectOne(queryWrapper);
        if (orders == null) {
            return 0.00;
        }
        return orders.getAmount();
    }

}
