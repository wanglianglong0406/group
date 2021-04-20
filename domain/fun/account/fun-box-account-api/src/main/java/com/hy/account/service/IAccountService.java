package com.hy.account.service;

import com.hy.account.model.Account;
import com.hy.account.model.AccountType;
import com.hy.pojo.ResponseJsonResult;
import io.swagger.annotations.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description: $-  -$ #--> 账户信息表 账户信息表(Account)表服务接口
 * @Author: 寒夜
 * @CreateDate: 2020/11/19 15:46
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/11/19 15:46
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Api(value = "账户中心接口", tags = "账户中心接口")
@RequestMapping("account-api/v1")
@FeignClient("fun-box-account-service")
public interface IAccountService {

    //查询账户类型列表
    @ApiOperation(value = "账户类型列表API", notes = "账户类型列表API", httpMethod = "GET")
    @GetMapping("/accountTypeInfoList")
    public ResponseJsonResult queryAccountTypeInfoList(@RequestParam("userId") @ApiParam(name = "userId", value = "用户id", required = true) String userId);


    @ApiOperation(value = "账户类型列表SPI", notes = "账户类型列表SPI", httpMethod = "GET")
    @GetMapping("/getAccountTypeInfoList")
    public List<AccountType> getAccountTypeInfoList();

    //转账
    @ApiOperation(value = "二级账户转入API", notes = "二级账户转入API", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String", required = true),
            @ApiImplicitParam(name = "accountType", value = "账户类型", dataType = "Integer ", required = true),
            @ApiImplicitParam(name = "amount", value = "金额", dataType = "Double", required = true)
    })
    @PostMapping("/transferIn")
    public ResponseJsonResult transferIn(@RequestParam("userId") String userId, @RequestParam("accountType") Integer accountType, @RequestParam("amount") Double amount);

    //查询锁定余额
    @ApiOperation(value = "查询锁钱包API", notes = "查询锁钱包API", httpMethod = "GET")
    @GetMapping("/lockWallet")
    public ResponseJsonResult lockWallet(@RequestParam("userId") @ApiParam(name = "userId", value = "用户id", required = true) String userId);

    //检索所有余额
    @ApiOperation(value = "二级账号转出API", notes = "二级装好转出API", httpMethod = "POST")
    @PostMapping("/transferOut")
    public ResponseJsonResult transferOut(@RequestParam("userId") @ApiParam(name = "userId", value = "用户id", required = true) String userId);


    //创建默认钱包账户
    @ApiOperation(value = "创建默认钱包账户API", notes = "创建默认钱包账户API", httpMethod = "POST", hidden = true)
    @PostMapping("/defaultAccount")
    public Account createDefaultAccount(@RequestParam("userId") @ApiParam(name = "userId", value = "用户id", required = true) String userId);

    //账户充值
    @ApiOperation(value = "账户充值API", notes = "账户充值API", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String", required = true),
            @ApiImplicitParam(name = "accountId", value = "账户id", dataType = "Long ", required = true),
            @ApiImplicitParam(name = "amount", value = "交易金额", dataType = "Double ", required = true),
            @ApiImplicitParam(name = "payMethod", value = "支付方式 1:UPI 2:账户余额,3:签到返佣 4 代理返佣 5 系统 6 转入  7 转出", dataType = "Integer", required = true),
            @ApiImplicitParam(name = "orderType", value = "订单类型1: 投注 2 充值 3 提现 4 签到奖励 5 代理佣金 6 升级奖励 7 月票红包 8 邀请奖励 9 首次存款奖励 10 被邀请者首笔存款返利", dataType = "Integer", required = true)
    })
    @PostMapping("/accountRecharge")
    public ResponseJsonResult accountRecharge(@RequestParam("userId") String userId, @RequestParam("amount") Double amount, @RequestParam("accountId") Long accountId, @RequestParam("payMethod") Integer payMethod, @RequestParam("orderType") Integer orderType);


    //钱包账户收入
    @ApiOperation(value = "钱包账户收入SPI", notes = "钱包账户收入SPI", httpMethod = "PUT", hidden = true)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String", required = true),
            @ApiImplicitParam(name = "amount", value = "交易金额", dataType = "Double ", required = true),
            @ApiImplicitParam(name = "payMethod", value = "支付方式 1:UPI 2:账户余额,3:签到返佣 4 代理返佣 5 系统", dataType = "Integer", required = true),
            @ApiImplicitParam(name = "orderType", value = "订单类型1: 投注 2 充值 3 提现 4 签到奖励 5 代理佣金 6 升级奖励 7 月票红包 8 邀请奖励 9 首次存款奖励 10 被邀请者首笔存款返利", dataType = "Integer", required = true)
    })
    @PutMapping("/recharge")
    public Double toRecharge(@RequestParam("userId") String userId, @RequestParam("amount") Double amount, @RequestParam("payMethod") Integer payMethod, @RequestParam("orderType") Integer orderType);

    //代理用户，下级用户首充返利给上级用户
    @ApiOperation(value = "代理用户，下级用户首充返利给上级用户SPI", notes = "代理用户，下级用户首充返利给上级用户SPI", httpMethod = "PUT", hidden = true)
    @PutMapping("/rechargeRebate")
    public void rechargeRebate(@RequestParam("userId") @ApiParam(name = "userId", value = "用户id", required = true) String userId);

    //增加签到奖励金额到账户
    @ApiOperation(value = "增加签到奖励金额到账户SPI", notes = "增加签到奖励金额到账户SPI", httpMethod = "PUT", hidden = true)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String", required = true),
            @ApiImplicitParam(name = "reward_amount", value = "签到奖励金额", dataType = "Double ", required = true),
            @ApiImplicitParam(name = "payMethod", value = "支付方式 1:UPI 2:账户余额,3:签到返佣 4 代理返佣 5 系统", dataType = "Integer", required = true),
            @ApiImplicitParam(name = "orderType", value = "订单类型1: 投注 2 充值 3 提现 4 签到奖励 5 代理佣金 6 升级奖励 7 月票红包 8 邀请奖励 9 首次存款奖励 10 被邀请者首笔存款返利", dataType = "Integer", required = true)
    })
    @PutMapping("/signInReward")
    public Boolean signInReward(@RequestParam("userId") String userId, @RequestParam("reward_amount") Double reward_amount, @RequestParam("payMethod") Integer payMethod, @RequestParam("orderType") Integer orderType);


    //充值返利
    @ApiOperation(value = "增加充值返利奖励金额到账户SPI", notes = "增加充值返利奖励金额到账户SPI", httpMethod = "PUT", hidden = true)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String", required = true),
            @ApiImplicitParam(name = "rechargeRebate", value = "充值返利", dataType = "Double ", required = true),
            @ApiImplicitParam(name = "payMethod", value = "支付方式 1:UPI 2:账户余额,3:签到返佣 4 代理返佣 5 系统", dataType = "Integer", required = true),
            @ApiImplicitParam(name = "orderType", value = "订单类型1: 投注 2 充值 3 提现 4 签到奖励 5 代理佣金 6 升级奖励 7 月票红包 8 邀请奖励 9 首次存款奖励 10 被邀请者首笔存款返利", dataType = "Integer", required = true)
    })
    @PutMapping("/rechargeRebateBlance")
    public Boolean rechargeRebateBlance(@RequestParam("userId") String userId, @RequestParam("rechargeRebate") Double rechargeRebate,
                                        @RequestParam("payMethod") Integer payMethod, @RequestParam("orderType") Integer orderType);

    //佣金奖励
    @ApiOperation(value = "增加充值返利奖励金额到账户SPI", notes = "增加充值返利奖励金额到账户SPI", httpMethod = "PUT", hidden = true)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String", required = true),
            @ApiImplicitParam(name = "commission", value = "佣金奖励", dataType = "Double ", required = true),
            @ApiImplicitParam(name = "payMethod", value = "支付方式 1:UPI 2:账户余额,3:签到返佣 4 代理返佣 5 系统", dataType = "Integer", required = true),
            @ApiImplicitParam(name = "orderType", value = "订单类型1: 投注 2 充值 3 提现 4 签到奖励 5 代理佣金 6 升级奖励 7 月票红包 8 邀请奖励 9 首次存款奖励 10 被邀请者首笔存款返利", dataType = "Integer", required = true)
    })
    @PutMapping("/commissionBlance")
    public Boolean commissionBlance(@RequestParam("userId") String userId, @RequestParam("commission") Double commission,
                                    @RequestParam("payMethod") Integer payMethod, @RequestParam("orderType") Integer orderType);



    //邀请奖励
    @ApiOperation(value = "增加邀请奖励金额到账户SPI", notes = "增加邀请奖励金额到账户SPI", httpMethod = "PUT", hidden = true)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String", required = true),
            @ApiImplicitParam(name = "invitationReward", value = "佣金奖励", dataType = "Double ", required = true),
            @ApiImplicitParam(name = "payMethod", value = "支付方式 1:UPI 2:账户余额,3:签到返佣 4 代理返佣 5 系统", dataType = "Integer", required = true),
            @ApiImplicitParam(name = "orderType", value = "订单类型1: 投注 2 充值 3 提现 4 签到奖励 5 代理佣金 6 升级奖励 7 月票红包 8 邀请奖励 9 首次存款奖励 10 被邀请者首笔存款返利", dataType = "Integer", required = true)
    })
    @PutMapping("/invitationRewardBlance")
    public Boolean invitationRewardBlance(@RequestParam("userId") String userId, @RequestParam("invitationReward") Double invitationReward,
                                    @RequestParam("payMethod") Integer payMethod, @RequestParam("orderType") Integer orderType);


    //增加VIP升级奖励金额到账户
    @ApiOperation(value = "增加VIP升级奖励金额到账户SPI", notes = "增加VIP升级奖励金额到账户SPI", httpMethod = "PUT", hidden = true)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String", required = true),
            @ApiImplicitParam(name = "upgradeReward", value = "月票红包金额", dataType = "Double ", required = true),
            @ApiImplicitParam(name = "payMethod", value = "支付方式 1:UPI 2:账户余额,3:签到返佣 4 代理返佣 5 系统", dataType = "Integer", required = true),
            @ApiImplicitParam(name = "orderType", value = "订单类型1: 投注 2 充值 3 提现 4 签到奖励 5 代理佣金 6 升级奖励 7 月票红包 8 邀请奖励 9 首次存款奖励 10 被邀请者首笔存款返利", dataType = "Integer", required = true)
    })
    @PutMapping("/upgradeRewardBlance")
    public Boolean upgradeRewardBlance(@RequestParam("userId") String userId, @RequestParam("monthlyTicket") Double upgradeReward,
                                       @RequestParam("payMethod") Integer payMethod, @RequestParam("orderType") Integer orderType);

    //增加月票红包到账户
    @ApiOperation(value = "增加签到奖励金额到账户SPI", notes = "增加签到奖励金额到账户SPI", httpMethod = "PUT", hidden = true)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String", required = true),
            @ApiImplicitParam(name = "monthlyTicket", value = "月票红包金额", dataType = "Double ", required = true),
            @ApiImplicitParam(name = "payMethod", value = "支付方式 1:UPI 2:账户余额,3:签到返佣 4 代理返佣 5 系统", dataType = "Integer", required = true),
            @ApiImplicitParam(name = "orderType", value = "订单类型1: 投注 2 充值 3 提现 4 签到奖励 5 代理佣金 6 升级奖励 7 月票红包 8 邀请奖励 9 首次存款奖励 10 被邀请者首笔存款返利", dataType = "Integer", required = true)
    })
    @PutMapping("/monthlyTicketBlance")
    public Boolean monthlyTicketBlance(@RequestParam("userId") String userId, @RequestParam("monthlyTicket") Double monthlyTicket,
                                       @RequestParam("payMethod") Integer payMethod, @RequestParam("orderType") Integer orderType);

    //钱包账户支出
    @ApiOperation(value = "支付API", notes = "支付API", httpMethod = "PUT", hidden = true)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "系统订单号", dataType = "Long", required = true),
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String", required = true),
            @ApiImplicitParam(name = "amount", value = "交易金额", dataType = "Double ", required = true),
            @ApiImplicitParam(name = "accountPassword", value = "账户支付密码", dataType = "String", required = true),
            @ApiImplicitParam(name = "payMethod", value = "支付方式 1:UPI 2:账户余额,3:签到返佣 4 代理返佣", dataType = "Integer", required = true),
            @ApiImplicitParam(name = "orderType", value = "订单类型1: 彩票 2 充值 3 提现 4 签到奖励 5 代理佣金", dataType = "Integer", required = true)
    })
    @PutMapping("/pay")
    public ResponseJsonResult pay(@RequestParam("userId") String userId, @RequestParam("orderId") Long orderId,
                                  @RequestParam("amount") Double amount, @RequestParam("accountPassword") String accountPassword,
                                  @RequestParam("payMethod") Integer payMethod, @RequestParam("orderType") Integer orderType);

    //后台账户自动支付
    @ApiOperation(value = "账户支付SPI", notes = "账户支付SPI", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "系统订单号", dataType = "Long", required = true),
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String", required = true),
            @ApiImplicitParam(name = "amount", value = "交易金额", dataType = "Double", required = true)
    })
    @PostMapping("/accountPayment")
    public ResponseJsonResult accountPayment(@RequestParam("userId") String userId, @RequestParam("amount") Double amount, @RequestParam("orderId") Long orderId);


    //校验账户支付密码
    @ApiOperation(value = "校验账户支付密码API", notes = "校验账户支付密码SPI", httpMethod = "GET", hidden = true)
    @GetMapping("/verfiyUserAccountPassword")
    public ResponseJsonResult checkUserAccountPassword(@RequestParam("userId") @ApiParam(name = "userId", value = "用户id", required = true) String userId,
                                                       @RequestParam("accountPassword") @ApiParam(name = "accountPassword", value = "账户支付密码", required = true) String accountPassword);

    //校验账户支付状态
    @ApiOperation(value = "校验账户状态API", notes = "校验账户账户状态SPI", httpMethod = "GET", hidden = true)
    @GetMapping("/verfiyUserAccountStatus")
    public ResponseJsonResult checkUserAccountStatus(@RequestParam("userId") @ApiParam(name = "userId", value = "用户id", required = true) String userId);

    //查询钱包账户余额
    @ApiOperation(value = "查询钱包账户余额API", notes = "查询钱包账户余额API", httpMethod = "GET")
    @GetMapping("/queryAccountBlance")
    public ResponseJsonResult queryAccountBlance(@RequestParam("userId") @ApiParam(name = "userId", value = "用户id", required = true) String userId);

    //查询主账户信息
    @ApiOperation(value = "查询主账户信息API", notes = "查询主账户信息API", httpMethod = "GET")
    @GetMapping("/queryAccountInfo")
    public ResponseJsonResult queryAccountInfo(@RequestParam("userId") @ApiParam(name = "userId", value = "用户id", required = true) String userId);

    @ApiOperation(value = "查询主账户信息SPI", notes = "查询主账户信息SPI", httpMethod = "GET")
    @GetMapping("/getAccountInfo")
    public Account getAccountInfo(@RequestParam("userId") @ApiParam(name = "userId", value = "用户id", required = true) String userId);

    //绑定银行卡后进行开户
    @ApiOperation(value = "开户", notes = "开户", httpMethod = "GET", hidden = true)
    @PutMapping("/account")
    public boolean openAccount(@RequestParam("userId") @ApiParam(name = "userId", value = "用户id", required = true) String userId,
                               @RequestParam("accountId") @ApiParam(name = "accountId", value = "账户ID", required = true) long accountId);


    @ApiOperation(value = "查询二级账户信息API", notes = "查询二级账户信息API", httpMethod = "GET")
    @GetMapping("/secondaryAccountInfo")
    public ResponseJsonResult querySecondaryAccountInfo(@RequestParam("userId") @ApiParam(name = "userId", value = "用户id", required = true) String userId);



    //统计邀请奖励以及返利奖励
    @ApiOperation(value = "统计邀请奖励以及返利奖励API", notes = "统计邀请奖励以及返利奖励API", httpMethod = "GET")
    @GetMapping("/invitationGiftAndRebateReward")
    public ResponseJsonResult totalInvitationGiftAndRebateReward(@ApiParam(value = "用户id", name = "userId", required = true) @RequestParam(value = "userId") String userId);


    //统计邀请奖励以及代理佣金奖励
    @ApiOperation(value = "统计邀请奖励以及代理佣金奖励API", notes = "统计邀请奖励以及代理佣金奖励API", httpMethod = "GET")
    @GetMapping("/invitationRewardAndCommissionReward")
    public ResponseJsonResult totalInvitationRewardAndCommissionReward(@ApiParam(value = "用户id", name = "userId", required = true) @RequestParam(value = "userId") String userId);

}
