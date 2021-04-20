package com.hy.user.service;

import com.hy.pojo.ResponseJsonResult;
import com.hy.user.model.AgencyLevel;
import com.hy.user.model.User;
import com.hy.user.model.UserAgentDistributionInfo;
import io.swagger.annotations.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/12/29 14:04
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/12/29 14:04
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Api(value = "用户信息相关接口", tags = "用户信息相关接口")
@RequestMapping("luckshop/user-api/v1")
@FeignClient("luck-shop-user-service")
public interface IUserService {


    @ApiOperation(value = "发送手机验证码API", notes = "发送手机验证码API", httpMethod = "GET")
    @GetMapping("verificationCode")
    public ResponseJsonResult sendVerificationCode(@RequestParam("mobilePhone") @ApiParam(value = "手机号", name = "mobilePhone", required = true) String mobilePhone);

    //更新用户等级
    @ApiOperation(value = "更新用户状态SPI", notes = "更新用户状态SPI", httpMethod = "PUT", hidden = true)
    @PutMapping("/updateUserStatus")
    public int updateUserStatus(@ApiParam(value = "用户id", name = "userId", required = true) @RequestParam("userId") String userId,
                                @ApiParam(value = "用户状态", name = "userStatus", required = true) @RequestParam("userStatus") Integer userStatus);

    //会员升级
    @ApiOperation(value = "会员升级SPI", notes = "会员升级SPI", httpMethod = "PUT", hidden = true)
    @PutMapping("/memberUpgrade")
    public int memberUpgrade(@ApiParam(value = "用户id", name = "userId", required = true) @RequestParam("userId") String userId);

    @ApiOperation(value = "会员降级SPI", notes = "会员升级SPI", httpMethod = "PUT", hidden = true)
    @PutMapping("/memberDemotion")
    public int memberDemotion(@ApiParam(value = "用户id", name = "userId", required = true) @RequestParam("userId") String userId);

    //查询用户信息
    @ApiOperation(value = "查询用户信息API", notes = "查询用户信息API", httpMethod = "GET")
    @GetMapping("/userInfo")
    public ResponseJsonResult findUserInfo(@ApiParam(value = "用户id", name = "userId", required = true) @RequestParam("userId") String userId);

    @ApiOperation(value = "查询用户信息SPI", notes = "查询用户信息SPI", httpMethod = "GET", hidden = true)
    @GetMapping("/userInfoByUserId")
    public User queryUserInfoByUserId(@ApiParam(value = "用户id", name = "userId", required = true) @RequestParam("userId") String userId);

    //查询用户信息列表
    @ApiOperation(value = "查询用户信息列表SPI", notes = "查询用户信息列表SPI", httpMethod = "GET", hidden = true)
    @GetMapping("/userInfoList")
    public List<User> querUserInfoList();

    //查询代理用户信息列表SPI
    @ApiOperation(value = "查询代理用户信息列表SPI", notes = "查询代理用户信息列表SPI", httpMethod = "GET", hidden = true)
    @GetMapping("/getUserAgentInfoList")
    public List<User> getUserAgentInfoList();

    @ApiOperation(value = "检查验证码是否正确SPI", notes = "检查验证码是否正确SPI", httpMethod = "GET", hidden = true)
    @GetMapping("/checkVerifcationCode")
    public ResponseJsonResult checkVerifcationCode(@RequestParam("mobilePhone") String mobilePhone, @RequestParam("verificationCode") String verificationCode);

    //通过注册码查询下级级用户信息SPI
    @ApiOperation(value = "通过注册码查询下级级用户信息API", notes = "通过注册码查询下级级用户信息API", httpMethod = "GET")
    @GetMapping("/querySubUserInfo")
    public ResponseJsonResult querySubUserInfo(@ApiParam(value = "注册码", name = "registrationCode", required = true) @RequestParam("registrationCode") String registrationCode);

    //通过注册码查询下级级用户信息SPI
    @ApiOperation(value = "通过注册码查询下级级用户信息SPI", notes = "通过注册码查询下级级用户信息SPI", httpMethod = "GET", hidden = true)
    @GetMapping("/queryUserInfoByRegistrationCode")
    public List<User> queryUserInfoByRegistrationCode(@ApiParam(value = "注册码", name = "registrationCode", required = true) @RequestParam("registrationCode") String registrationCode);

    //通过邀请码查询上级用户信息
    @ApiOperation(value = "通过邀请码查询上级用户信息SPI", notes = "通过邀请码查询上级用户信息SPI", httpMethod = "GET", hidden = true)
    @GetMapping("/queryUserInfoByInvitationCode")
    public User queryUserInfoByInvitationCode(@ApiParam(value = "邀请码", name = "invitationCode", required = true) @RequestParam("invitationCode") String invitationCode);


    //更新团队规模
    @ApiOperation(value = "更新团队规模数据大小SPI", notes = "更新团队规模数据大小SPI", httpMethod = "PUT", hidden = true)
    @PutMapping("/updateUserTeamSize")
    public int updateUserTeamSize(@RequestParam("invitationCode") String invitationCode);

    //更新代理等级
    @ApiOperation(value = "更新代理等级SPI", notes = "更新代理等级SPI", httpMethod = "PUT", hidden = true)
    @PutMapping("/updateUserAgencyLevel")
    public int updateUserAgencyLevel(@RequestParam("userId") String userId, @RequestParam("agencyLevel") Integer agencyLevel);

    //代理等级信息列表
    @ApiOperation(value = "代理等级信息列表SPI", notes = "代理等级信息列表SPI", httpMethod = "GET", hidden = true)
    @GetMapping("/getAgencyLevelInfoList")
    public List<AgencyLevel> getAgencyLevelInfoList();

    //用户分销代理信息
    @ApiOperation(value = "用户分销代理信息SPI", notes = "用户分销代理信息SPI", httpMethod = "GET", hidden = true)
    @GetMapping("/getUserAgentDistributionInfoList")
    public List<UserAgentDistributionInfo> getUserAgentDistributionInfoList();

    //用户分销代理信息
    @ApiOperation(value = "用户分销代理信息SPI", notes = "用户分销代理信息SPI", httpMethod = "GET", hidden = true)
    @GetMapping("/getUserAgentDistributionInfoByVipLevelAndAgencyLevel")
    public UserAgentDistributionInfo getUserAgentDistributionInfoByVipLevelAndAgencyLevel(@RequestParam("vipLevel") Integer vipLevel, @RequestParam("agencyLevel") Integer agencyLevel);


    //查询用户列表
    @ApiOperation(value = "查询用户列表API", notes = "查询用户列表API", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "当前页数", dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize", value = "每页的数量", dataType = "Integer"),
            @ApiImplicitParam(name = "startDate", value = "开始日期", dataType = "String"),
            @ApiImplicitParam(name = "endDate", value = "结束日期", dataType = "String")
    })
    @GetMapping("/getUserInfoPagedGridResult")
    public ResponseJsonResult getUserInfoPagedGridResult(@RequestParam(value = "pageNo", required = false) Integer pageNo, @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                @RequestParam(value = "startDate", required = false) String startDate, @RequestParam(value = "endDate", required = false) String endDate);


}
