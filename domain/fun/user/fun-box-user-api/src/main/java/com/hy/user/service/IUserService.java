package com.hy.user.service;

import com.hy.pojo.PagedGridResult;
import com.hy.pojo.ResponseJsonResult;
import com.hy.user.model.User;
import io.swagger.annotations.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户信息表 用户信息表(User)表服务接口
 *
 * @author hanye
 * @since 2020-11-17 13:55:05
 */
@Api(value = "用户服务中心相关API", tags = "用户中心相关API")
@RequestMapping("user-api/v1")
@FeignClient("fun-box-user-service")
public interface IUserService {


    /**
     * 发送手机验证码
     *
     * @param userMobilePhone
     * @return
     */

    @ApiOperation(value = "发送手机验证码", notes = "发送手机验证码", httpMethod = "GET")
    @GetMapping("verificationCode")
    public ResponseJsonResult sendVerificationCode(@RequestParam("userMobilePhone") @ApiParam(value = "手机号", name = "userMobilePhone", required = true) String userMobilePhone);

    //更新用户等级
    @ApiOperation(value = "更新用户状态SPI", notes = "更新用户状态SPI", httpMethod = "PUT")
    @PutMapping("/updateUserStatus")
    public int updateUserStatus(@ApiParam(value = "用户id", name = "userId", required = true) @RequestParam("userId") String userId,
                                @ApiParam(value = "用户状态", name = "userStatus", required = true) @RequestParam("userStatus") Integer userStatus);

    //会员升级
    @ApiOperation(value = "会员升级SPI", notes = "会员升级SPI", httpMethod = "PUT")
    @PutMapping("/memberUpgrade")
    public int memberUpgrade(@ApiParam(value = "用户id", name = "userId", required = true) @RequestParam("userId") String userId);

    @ApiOperation(value = "会员降级SPI", notes = "会员升级SPI", httpMethod = "PUT")
    @PutMapping("/memberDemotion")
    public int memberDemotion(@ApiParam(value = "用户id", name = "userId", required = true) @RequestParam("userId") String userId);

    //查询用户信息
    @ApiOperation(value = "查询用户信息", notes = "查询用户信息", httpMethod = "GET")
    @GetMapping("/userInfo")
    public ResponseJsonResult findUserInfo(@ApiParam(value = "用户id", name = "userId", required = true) @RequestParam("userId") String userId);

    @ApiOperation(value = "查询用户信息SPI", notes = "查询用户信息SPI", httpMethod = "GET")
    @GetMapping("/userInfoByUserId")
    public User queryUserInfoByUserId(@ApiParam(value = "用户id", name = "userId", required = true) @RequestParam("userId") String userId);

    //查询用户信息列表
    @ApiOperation(value = "查询用户信息列表SPI", notes = "查询用户信息列表SPI", httpMethod = "GET")
    @GetMapping("/userInfoList")
    public List<User> querUserInfoList();

    //通过邀请码查询上级用户信息
    @ApiOperation(value = "通过邀请码查询上级用户信息SPI", notes = "通过邀请码查询上级用户信息SPI", httpMethod = "GET", hidden = true)
    @GetMapping("/queryUserInfoByUserInvitationCode")
    public User queryUserInfoByUserInvitationCode(@ApiParam(value = "邀请码", name = "invitationCode", required = true) @RequestParam("invitationCode") String invitationCode);


    //校验验证码
    @ApiOperation(value = "校验验证码", notes = "校验验证码", httpMethod = "get",hidden = true)
    @GetMapping("/checkVerifcationCode")
    public ResponseJsonResult checkVerifcationCode(String userMobilePhone, String verificationCode);

    @ApiOperation(value = "修改用户手机号", notes = "修改用户手机号", httpMethod = "PUT")
    @PutMapping("/mobilePhone")
    public ResponseJsonResult modifyMobilePhone(@ApiParam(value = "用户id", name = "userId", required = true)
                                                @RequestParam("userId") String userId,
                                                @ApiParam(value = "用户手机号", name = "userMobilePhone", required = true)
                                                @RequestParam("userMobilePhone") String userMobilePhone,
                                                @ApiParam(value = "手机验证码", name = "verificationCode", required = true)
                                                @RequestParam("verificationCode") String verificationCode);


    @ApiOperation(value = "修改用户登录密码", notes = "修改用户登录密码", httpMethod = "PUT")
    @PutMapping("/loginPassword")
    public ResponseJsonResult modifyLoginPassword(@ApiParam(value = "用户id", name = "userId", required = true)
                                                  @RequestParam("userId") String userId,
                                                  @ApiParam(value = "用户登录密码", name = "userLoginPassword", required = true)
                                                  @RequestParam("userLoginPassword") String userLoginPassword,
                                                  @ApiParam(value = "用户手机号", name = "userMobilePhone", required = true)
                                                  @RequestParam("userMobilePhone") String userMobilePhone,
                                                  @ApiParam(value = "手机验证码", name = "verificationCode", required = true)
                                                  @RequestParam("verificationCode") String verificationCode);

    @ApiOperation(value = "修改地址信息", notes = "修改用户登录密码", httpMethod = "PUT", hidden = true)
    @PostMapping("loginAddress")
    public void modifyUserAddress(@RequestParam("userId") String userId);


    //查询邀请记录信息
    @ApiOperation(value = "邀请记录信息API", notes = "邀请记录信息API", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String", required = true),
            @ApiImplicitParam(name = "pageNo", value = "当前页数", dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize", value = "每页的数量", dataType = "Integer"),
            @ApiImplicitParam(name = "startDate", value = "开始日期", dataType = "String"),
            @ApiImplicitParam(name = "endDate", value = "结束日期", dataType = "String")
    })
    @GetMapping("/invitationRecordInfoList")
    public PagedGridResult queryInvitationRecordInfoList(@RequestParam(value = "userId") String userId,
                                                         @RequestParam(value = "pageNo", required = false) Integer pageNo, @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                         @RequestParam(value = "startDate", required = false) String startDate, @RequestParam(value = "endDate", required = false) String endDate);



}
