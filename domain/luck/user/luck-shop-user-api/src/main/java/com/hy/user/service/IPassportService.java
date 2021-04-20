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
@RequestMapping("luckshop/user-api/passport/v1")
@FeignClient("luck-shop-user-service")
public interface IPassportService {

    //注册
    @ApiOperation(value = "注册API", notes = "注册API", httpMethod = "POST")
    @PostMapping("/regist")
    public ResponseJsonResult regist(@RequestBody UserBO userBO, HttpServletRequest request, HttpServletResponse response);


    //登录
    @ApiOperation(value = "登录API", notes = "登录API", httpMethod = "GET")
    @GetMapping("/login")
    public ResponseJsonResult login(@ApiParam(value = "手机号", name = "mobilePhone", required = true) @RequestParam("mobilePhone") String mobilePhone,
                                    @ApiParam(value = "密码", name = "password", required = true) @RequestParam("password") String password,
                                    HttpServletRequest request, HttpServletResponse response) throws Exception;


    }



