package com.hy.manager.service;

import com.hy.pojo.ResponseJsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2021/1/30 13:14
 * @UpdateUser: 寒夜
 * @UpdateDate: 2021/1/30 13:14
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Api(value = "管理平台-->订单管理中心相关接口", tags = "管理平台-->订单管理中心相关接口")
@RequestMapping("manager/order-management-center/v1")
@FeignClient("manager-service")
public interface IOrderManagementCenterService {

    //查询用户列表
    @ApiOperation(value = "<幸运商店>查询订单列表API", notes = "<幸运商店>查询订单列表API", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String", required = true),
            @ApiImplicitParam(name = "orderType", value = "订单类型", dataType = "Integer"),
            @ApiImplicitParam(name = "orderId", value = "订单号", dataType = "Long"),
            @ApiImplicitParam(name = "userIds", value = "用户ID", dataType = "String[]"),
            @ApiImplicitParam(name = "pageNo", value = "当前页数", dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize", value = "每页的数量", dataType = "Integer"),
            @ApiImplicitParam(name = "startDate", value = "开始日期", dataType = "String"),
            @ApiImplicitParam(name = "endDate", value = "结束日期", dataType = "String")
    })
    @GetMapping("/queryUserInfoList")
    public ResponseJsonResult queryOrderInfoList(
            @RequestParam(value = "orderType", required = false) Integer orderType,
            @RequestParam(value = "orderId", required = false) Long orderId,
            @RequestParam(value = "userIds", required = false) String[] userIds,
            @RequestParam(value = "userId") String userId,
            @RequestParam(value = "pageNo", required = false) Integer pageNo,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate);


}
