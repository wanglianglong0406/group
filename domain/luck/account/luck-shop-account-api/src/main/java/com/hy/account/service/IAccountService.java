package com.hy.account.service;

import com.hy.account.model.Account;
import com.hy.pojo.ResponseJsonResult;
import io.swagger.annotations.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/12/29 17:20
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/12/29 17:20
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Api(value = "账户中心相关接口", tags = "账户中心相关接口")
@RequestMapping("luckshop/account-api/v1")
@FeignClient("luck-shop-account-service")
public interface IAccountService {


    //提交订单任务获取佣金API
    @ApiOperation(value = "提交订单任务获取佣金API", notes = "提交订单任务获取佣金API", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String", required = true),
            @ApiImplicitParam(name = "orderId", value = "订单ID", dataType = "Long", required = true),
            @ApiImplicitParam(name = "rewarsAmount", value = "佣金", dataType = "Double", required = true)
    })
    @PostMapping("/submitOrderTask")
    public ResponseJsonResult submitOrderTask(@RequestParam("userId") String userId, @RequestParam("orderId") Long orderId, @RequestParam("rewarsAmount") Double rewarsAmount);

    @ApiOperation(value = "体验金清零", notes = "体验金清零", httpMethod = "PUT", hidden = true)
    @PutMapping("/experienceZero")
    public void experienceZero(@RequestParam("userId") String userId);

    @ApiOperation(value = "体验金清零", notes = "体验金清零", httpMethod = "PUT", hidden = true)
    @PutMapping("/zeroReturnToday")
    public void zeroReturnToday(@RequestParam("userId") String userId);

    //创建默认钱包账户
    @ApiOperation(value = "创建默认钱包账户API", notes = "创建默认钱包账户API", httpMethod = "POST", hidden = true)
    @PostMapping("/defaultAccount")
    public Account createDefaultAccount(@RequestParam("userId") @ApiParam(name = "userId", value = "用户id", required = true) String userId);

    //转账
    @ApiOperation(value = "转入API", notes = "转入API", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String", required = true),
            @ApiImplicitParam(name = "productId", value = "产品ID", dataType = "Integer ", required = true),
            @ApiImplicitParam(name = "amount", value = "金额", dataType = "Double", required = true),
            @ApiImplicitParam(name = "orderToken", value = "订单token", dataType = "String")
    })
    @PostMapping("/transferIn")
    public ResponseJsonResult transferIn(@RequestParam("orderToken") String orderToken, @RequestParam("userId") String userId, @RequestParam("productId") Integer productId, @RequestParam("amount") Double amount);

    //理财产品T+1结算
    @ApiOperation(value = "理财产品T+1结算SPI", notes = "理财产品T+1结算SPI", httpMethod = "PUT", hidden = true)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String", required = true),
            @ApiImplicitParam(name = "dailyInterestAmount", value = "日息", dataType = "Double", required = true)
    })
    @PutMapping("/financialSettlementOfTPlusOne")
    public boolean financialSettlementOfTPlusOne(@RequestParam("userId") String userId, @RequestParam("dailyInterestAmount") Double dailyInterestAmount);

    //检索所有余额
    @ApiOperation(value = "转出API", notes = "转出API", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String", required = true),
            @ApiImplicitParam(name = "productId", value = "产品ID", dataType = "Integer ", required = true),
            @ApiImplicitParam(name = "amount", value = "金额", dataType = "Double", required = true),
            @ApiImplicitParam(name = "orderToken", value = "订单token", dataType = "String")
    })
    @PostMapping("/transferOut")
    public ResponseJsonResult transferOut(@RequestParam("orderToken") String orderToken, @RequestParam("userId") String userId, @RequestParam("productId") Integer productId, @RequestParam("amount") Double amount);


    //我的账户信息API
    @ApiOperation(value = "我的账户信息API", notes = "我的账户信息API", httpMethod = "GET")
    @GetMapping("/myAccountInfo")
    public ResponseJsonResult queryMyAccountInfo(@RequestParam("userId") @ApiParam(name = "userId", value = "用户id", required = true) String userId);

    //查询主账户信息
    @ApiOperation(value = "查询账户信息SPI", notes = "查询账户信息SPI", httpMethod = "GET", hidden = true)
    @GetMapping("/getAccountInfo")
    public Account getAccountInfo(@RequestParam("userId") @ApiParam(name = "userId", value = "用户id", required = true) String userId);


    //增加奖励金额到账户
    @ApiOperation(value = "增加奖励SPI", notes = "增加奖励SPI", httpMethod = "PUT", hidden = true)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String", required = true),
            @ApiImplicitParam(name = "rewarsAmount", value = "奖励金额", dataType = "Double", required = true)
    })
    @PutMapping("/increaseRewards")
    public boolean increaseRewards(@RequestParam("userId") String userId, @RequestParam("rewarsAmount") Double rewarsAmount);
}
