package com.hy.user.service;

import com.hy.pojo.ResponseJsonResult;
import com.hy.user.model.bo.UserBO;
import com.hy.user.model.bo.center.AddressBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/12/29 13:21
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/12/29 13:21
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Api(value = "用户中心相关接口", tags = "用户中心相关接口")
@RequestMapping("luckshop/user-api/user-center/v1")
@FeignClient("luck-shop-user-service")
public interface IUserCenterService {

    //设置用户登录密码
    @ApiOperation(value = "设置用户登录密码API", notes = "设置用户登录密码API", httpMethod = "POST")
    @PostMapping("/setUserPassword")
    public ResponseJsonResult setUserPassword(@ApiParam(value = "用户id", name = "userId", required = true) @RequestParam("userId") String userId, @RequestBody UserBO userBO);

    //设置用户登录密码
    @ApiOperation(value = "设置用户昵称API", notes = "设置用户昵称API", httpMethod = "POST")
    @PostMapping("/setUserNicknames")
    public ResponseJsonResult setUserNicknames(@ApiParam(value = "用户id", name = "userId", required = true) @RequestParam("userId") String userId,
                                               @ApiParam(value = "用户昵称", name = "nicknames", required = true) @RequestParam("nicknames") String nicknames);

    //退出
    @ApiOperation(value = "退出API", notes = "退出API", httpMethod = "POST")
    @PostMapping("/signOut")
    public ResponseJsonResult signOut(@ApiParam(value = "用户id", name = "userId", required = true) @RequestParam("userId") String userId, HttpServletRequest request, HttpServletResponse response);

    //添加地址信息
    @ApiOperation(value = "添加地址信息API", notes = "添加地址信息API", httpMethod = "POST")
    @PostMapping("/addUserAddressInfo")
    public ResponseJsonResult addUserAddressInfo(@ApiParam(value = "用户id", name = "userId", required = true) @RequestParam("userId") String userId, @RequestBody AddressBO addressBO);

    //我的地址信息列表
    @ApiOperation(value = "我的地址列表API", notes = "我的地址列表API", httpMethod = "GET")
    @GetMapping("/myAddressList")
    public ResponseJsonResult queryMyAddressList(@ApiParam(value = "用户id", name = "userId", required = true) @RequestParam("userId") String userId, HttpServletRequest request, HttpServletResponse response);

}
