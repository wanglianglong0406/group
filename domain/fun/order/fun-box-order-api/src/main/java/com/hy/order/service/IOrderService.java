package com.hy.order.service;

import com.hy.order.model.Orders;
import com.hy.order.model.bo.OrderBO;
import com.hy.pojo.PagedGridResult;
import com.hy.pojo.ResponseJsonResult;
import io.swagger.annotations.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description: $- IOrderService 订单中心相关接口-$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/11/24 17:10
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/11/24 17:10
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Api(value = "订单相关接口", tags = "订单相关接口")
@RequestMapping("order-api/v1")
@FeignClient("fun-box-order-service")
public interface IOrderService {
    //查询系统订单信息
    @ApiOperation(value = "获取系统订单信息列表", notes = "获取系统订单信息列表", httpMethod = "GET", hidden = true)
    @GetMapping("/orderInfo")
    public ResponseJsonResult queryOrderInfo();

    //统计用户消费总流水
    @ApiOperation(value = "统计用户投注总流水SPI", notes = "统计用户投注总流水SPI", httpMethod = "GET", hidden = true)
    @GetMapping("/totalCashFlow")
    public Double totalCashFlow(@RequestParam(value = "userId") String userId);

    //统计基金流水
    @ApiOperation(value = "统计基金流水总金额API", notes = "统计基金流水总金额API", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String", required = true),
            @ApiImplicitParam(name = "platformId", value = "平台ID", dataType = "Integer", required = true),
            @ApiImplicitParam(name = "startDate", value = "开始日期", dataType = "String"),
            @ApiImplicitParam(name = "endDate", value = "结束日期", dataType = "String")
    })
    @GetMapping("/totalFundsStatisticsFlow")
    public ResponseJsonResult totalFundsStatisticsFlow(@RequestParam(value = "userId") String userId, @RequestParam(value = "platformId") Integer platformId,
                                                       @RequestParam(value = "startDate", required = false) String startDate,
                                                       @RequestParam(value = "endDate", required = false) String endDate);

    //统计用户充值总流水
    @ApiOperation(value = "统计用户充值总流水SPI", notes = "统计用户充值总流水SPI", httpMethod = "GET", hidden = true)
    @GetMapping("/totalRechargeFlow")
    public Double totalRechargeFlow(@RequestParam(value = "userId") String userId);

    //统计用户某一段日期内的总流水
    @ApiOperation(value = "统计用户某一段日期内的总流水SPI", notes = "统计用户某一段日期内的总流水SPI", httpMethod = "GET", hidden = true)
    @GetMapping("/totalAdateFlow")
    public Double totalAdateFlow(@RequestParam(value = "userId") String userId, @RequestParam(value = "startDate") String startDate,
                                 @RequestParam(value = "endDate") String endDate);

    //查询用户当月的充值流水信息
    @ApiOperation(value = "查询用户当月的充值流水信息SPI", notes = "查询用户当月的充值流水信息SPI", httpMethod = "GET", hidden = true)
    @GetMapping("/totalThisMonthRechargeFlow")
    public List<Orders> totalThisMonthRechargeFlow(@RequestParam(value = "userId") String userId, @RequestParam(value = "month") String month);

    //创建系统订单信息
    @ApiOperation(value = "初始创建订单信息SPI", notes = "初始创建订单信息SPI", httpMethod = "POST", hidden = true)
    @PostMapping("/createOrderInfo")
    public Long createOrderInfo(@RequestBody OrderBO orderBO);

    //统计用户当天的充值金额
    @ApiOperation(value = "统计用户当天充值金额SPI", notes = "统计用户当天充值金额SPI", httpMethod = "GET", hidden = true)
    @GetMapping("/totalRechargeAmountByDay")
    public Double totalRechargeAmount(@RequestParam(value = "userId") String userId);

    //同步订单信息
    @ApiOperation(value = "同步订单信息SPI", notes = "同步订单信息SPI", httpMethod = "PUT", hidden = true)
    @PutMapping("/updateOrderInfo")
    public boolean updateOrderInfo(@RequestParam(value = "orderId") Long orderId, @RequestParam(value = "orderStatus") Integer orderStatus,
                                   @RequestParam(value = "orderStatusName") String orderStatusName, @RequestParam(value = "extand") String extand);


    //同步投注订单输赢结果状态S
    @ApiOperation(value = "同步投注订单输赢结果状态SPI", notes = "同步投注订单输赢结果状态SPI", httpMethod = "PUT", hidden = true)
    @PutMapping("/updateBetOrderInfoWinOrLosses")
    public boolean updateBetOrderInfoWinOrLosses(@RequestParam(value = "orderId") Long orderId, @RequestParam(value = "isLuck") Integer isLuck, @RequestParam(value = "winOrLosse") Double winOrLosse);


    //查询投注记录订单信息
    @ApiOperation(value = "查询所有平台投注记录信息API", notes = "查询所有平台投注记录信息API", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String", required = true),
            @ApiImplicitParam(name = "platformId", value = "平台ID", dataType = "Integer"),
            @ApiImplicitParam(name = "pageNo", value = "当前页数", dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize", value = "每页的数量", dataType = "Integer"),
            @ApiImplicitParam(name = "startDate", value = "开始日期", dataType = "String"),
            @ApiImplicitParam(name = "endDate", value = "结束日期", dataType = "String")
    })
    @GetMapping("/betOrderInfoList")
    public PagedGridResult queryBetOrderInfoList(@RequestParam(value = "userId") String userId, @RequestParam(value = "platformId", required = false) Integer platformId,
                                                 @RequestParam(value = "pageNo", required = false) Integer pageNo,
                                                 @RequestParam(value = "pageSize", required = false) Integer pageSize, @RequestParam(value = "startDate", required = false) String startDate,
                                                 @RequestParam(value = "endDate", required = false) String endDate);


    //查询一般记录
    @ApiOperation(value = "查询一般交易记录信息API", notes = "查询一般交易记录信息API", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String", required = true),
            @ApiImplicitParam(name = "platformId", value = "平台ID", dataType = "Integer"),
            @ApiImplicitParam(name = "pageNo", value = "当前页数", dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize", value = "每页的数量", dataType = "Integer"),
            @ApiImplicitParam(name = "startDate", value = "开始日期", dataType = "String"),
            @ApiImplicitParam(name = "endDate", value = "结束日期", dataType = "String")
    })
    @GetMapping("/generalRecordInfoList")
    public PagedGridResult queryGeneralRecordInfoList(@RequestParam(value = "userId") String userId, @RequestParam(value = "orderType", required = false) Integer orderType,
                                                      @RequestParam(value = "pageNo", required = false) Integer pageNo,
                                                      @RequestParam(value = "pageSize", required = false) Integer pageSize, @RequestParam(value = "startDate", required = false) String startDate,
                                                      @RequestParam(value = "endDate", required = false) String endDate);

    //统计转入转出总金额
    @ApiOperation(value = "统计转入转出总金额API", notes = "统计转入转出总金额API", httpMethod = "GET")
    @GetMapping("/totalTransfer")
    public ResponseJsonResult totalTransfer(@ApiParam(value = "用户ID", name = "userId", required = true) @RequestParam(value = "userId") String userId);


    //查询充值和提现订单记录
    @ApiOperation(value = "查询充值和提现订单信息API", notes = "查询充值和提现订单信息API", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String", required = true),
            @ApiImplicitParam(name = "userName", value = "用户名称", dataType = "String"),
            @ApiImplicitParam(name = "pageNo", value = "当前页数", dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize", value = "每页的数量", dataType = "Integer"),
            @ApiImplicitParam(name = "startDate", value = "开始日期", dataType = "String"),
            @ApiImplicitParam(name = "endDate", value = "结束日期", dataType = "String")
    })
    @GetMapping("/depositAndWithdrawInfoList")
    public PagedGridResult queryDepositAndWithdrawInfoList(@RequestParam(value = "userId") String userId, @RequestParam(value = "userName") String userName,
                                                           @RequestParam(value = "pageNo", required = false) Integer pageNo, @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                           @RequestParam(value = "startDate", required = false) String startDate, @RequestParam(value = "endDate", required = false) String endDate);


    //统计存款总金额和取款总金额
    @ApiOperation(value = "统计存款总金额和取款总金额API", notes = "统计存款总金额和取款总金额API", httpMethod = "GET")
    @GetMapping("/totalDepositAndWithdraw")
    public ResponseJsonResult totalDepositAndWithdraw(@ApiParam(value = "用户id", name = "userId", required = true) @RequestParam(value = "userId") String userId);

}
