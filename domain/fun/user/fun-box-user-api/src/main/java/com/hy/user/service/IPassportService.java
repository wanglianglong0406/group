package com.hy.user.service;

import com.hy.pojo.ResponseJsonResult;
import com.hy.user.model.bo.UserBO;
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
 * @CreateDate: 2020/12/26 13:35
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/12/26 13:35
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Api(value = "注册登录相关接口", tags = "注册登录相关接口")
@RequestMapping("user-api/v1")
@FeignClient("fun-box-user-service")
public interface IPassportService {



    @ApiOperation(value = "用于校验用户是否存在", notes = "用于校验用户是否存在", httpMethod = "GET")
    @GetMapping("/checkUserName")
    public ResponseJsonResult usernameIsExit(@RequestParam(required = false) @ApiParam(value = "用户名", name = "userName", required = true) String userName);



    //注册
    @ApiOperation(value = "注册", notes = "用户注册(注册完成用户信息会放到cookie中 --[user] )", httpMethod = "POST")
    @PostMapping("/regist")
    public ResponseJsonResult regist(@RequestBody UserBO userBo, HttpServletRequest request, HttpServletResponse response);


    //登录
    @ApiOperation(value = "登录 ", notes = "用户登录(登录完成用户信息会放到cookie中 --[user])", httpMethod = "GET")
    @GetMapping("/login")
    public ResponseJsonResult login(@ApiParam(value = "用户名", name = "userName", required = true) @RequestParam("userName") String userName,
                                    @ApiParam(value = "用户密码", name = "userLoginPassword", required = true) @RequestParam("userLoginPassword") String userLoginPassword,
                                    HttpServletRequest request, HttpServletResponse response) throws Exception;


    //退出
    @ApiOperation(value = "退出", notes = "用户退出登录", httpMethod = "POST")
    @PostMapping("/logout")
    public ResponseJsonResult logout(HttpServletRequest request, HttpServletResponse response, @ApiParam(value = "用户id", name = "userId", required = true)
    @RequestParam("userId") String userId);


    //修改用户信息
    @ApiOperation(value = "修改用户信息", notes = "编辑用户信息", httpMethod = "PUT")
    @PutMapping("/update")
    public ResponseJsonResult update(@ApiParam(value = "用户id", name = "userId", required = true) @RequestParam("userId") String userId,
                                     @RequestBody UserBO userBO, HttpServletRequest request, HttpServletResponse response);

}
