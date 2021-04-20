package com.hy.order.service;

import com.hy.order.model.Orders;
import com.hy.order.model.bo.OrderBO;
import com.hy.pojo.ResponseJsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
@RequestMapping("luckshop/order-api/v1")
@FeignClient("luck-shop-order-service")
public interface IOrderService {


    //获取订单token
    @ApiOperation(value = "获取订单token API", notes = "获取订单token API", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String", required = true)
    })
    @GetMapping("/orderToken")
    public ResponseJsonResult getOrderToken(@RequestParam(value = "userId") String userId);

    @ApiOperation(value = "订单任务抢单API", notes = "订单任务抢单API", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String", required = true),
            @ApiImplicitParam(name = "orderToken", value = "订单token", dataType = "String")
    })
    @PostMapping("/orderGrabbingTask")
    public ResponseJsonResult orderGrabbingTask(@RequestParam(value = "userId") String userId, @RequestParam(value = "orderToken") String orderToken);

    //创建系统订单信息
    @ApiOperation(value = "初始创建订单信息SPI", notes = "初始创建订单信息SPI", httpMethod = "POST", hidden = true)
    @PostMapping("/createOrderInfo")
    public Long createOrderInfo(@RequestBody OrderBO orderBO);


    //同步订单信息
    @ApiOperation(value = "同步订单信息SPI", notes = "同步订单信息SPI", httpMethod = "POST", hidden = true)
    @PostMapping("/updateOrderInfo")
    public boolean updateOrderInfo(@RequestParam(value = "orderId") Long orderId, @RequestParam(value = "orderStatus") Integer orderStatus,
                                   @RequestParam(value = "orderStatusName") String orderStatusName, @RequestParam(value = "extand") String extand);



    //查询订单任务记录
    @ApiOperation(value = "查询订单任务记录API", notes = "查询订单任务记录API", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String", required = true),
            @ApiImplicitParam(name = "orderStauts", value = "10 挂起 30 成功", dataType = "Integer", required = true),
            @ApiImplicitParam(name = "pageNo", value = "当前页数", dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize", value = "每页的数量", dataType = "Integer"),
            @ApiImplicitParam(name = "startDate", value = "开始日期", dataType = "String"),
            @ApiImplicitParam(name = "endDate", value = "结束日期", dataType = "String")
    })
    @GetMapping("/queryTaskOrderRecordList")
    public ResponseJsonResult queryTaskOrderRecordList(@RequestParam(value = "userId") String userId, @RequestParam(value = "orderStauts") Integer orderStauts,
                                                       @RequestParam(value = "pageNo", required = false) Integer pageNo, @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                       @RequestParam(value = "startDate", required = false) String startDate, @RequestParam(value = "endDate", required = false) String endDate);


    //查询用户充值记录和提现记录
    @ApiOperation(value = "查询用户充值记录和提现记录API", notes = "查询用户充值记录和提现记录API", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String", required = true),
            @ApiImplicitParam(name = "orderType", value = "订单类型（2 充值 3 提现）", dataType = "Integer"),
            @ApiImplicitParam(name = "pageNo", value = "当前页数", dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize", value = "每页的数量", dataType = "Integer"),
            @ApiImplicitParam(name = "startDate", value = "开始日期", dataType = "String"),
            @ApiImplicitParam(name = "endDate", value = "结束日期", dataType = "String")
    })
    @GetMapping("/queryRechargeAndWithdrawOrderRecordList")
    public ResponseJsonResult queryRechargeAndWithdrawOrderRecordList(@RequestParam(value = "userId") String userId, @RequestParam(value = "orderType") Integer orderType,
                                                                      @RequestParam(value = "pageNo", required = false) Integer pageNo, @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                                      @RequestParam(value = "startDate", required = false) String startDate, @RequestParam(value = "endDate", required = false) String endDate);


    //获取第一笔充值订单信息SPI
    @ApiOperation(value = "获取第一笔充值订单信息SPI", notes = "获取第一笔充值订单信息SPI", httpMethod = "GET", hidden = true)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String", required = true)
    })
    @GetMapping("/getUserFirstRechargeInfo")
    public Orders getUserFirstRechargeInfo(@RequestParam(value = "userId") String userId);


    //查询订单列表
    @ApiOperation(value = "查询用户列表API", notes = "查询用户列表API", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderType", value = "订单类型", dataType = "Integer"),
            @ApiImplicitParam(name = "orderId", value = "订单号", dataType = "Long"),
            @ApiImplicitParam(name = "userIds", value = "用户ID", dataType = "String[]"),
            @ApiImplicitParam(name = "pageNo", value = "当前页数", dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize", value = "每页的数量", dataType = "Integer"),
            @ApiImplicitParam(name = "startDate", value = "开始日期", dataType = "String"),
            @ApiImplicitParam(name = "endDate", value = "结束日期", dataType = "String")
    })
    @GetMapping("/getOrderInfoPagedGridResult")
    public ResponseJsonResult getOrderInfoPagedGridResult(
            @RequestParam(value = "orderType", required = false) Integer orderType,
            @RequestParam(value = "orderId", required = false) Long orderId,
            @RequestParam(value = "userIds", required = false) String[] userIds,
            @RequestParam(value = "pageNo", required = false) Integer pageNo,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate);

    //获取理财结算订单信息SPI
    @ApiOperation(value = "获取理财结算订单信息SPI", notes = "获取理财结算订单信息SPI", httpMethod = "GET", hidden = true)
    @GetMapping("/getFinancialOrderInfoList")
    public List<Orders> getFinancialOrderInfoList();
}
