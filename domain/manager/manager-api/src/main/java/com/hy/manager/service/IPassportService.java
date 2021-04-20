package com.hy.manager.service;

import com.hy.manager.model.bo.SystemManagerUserBO;
import com.hy.pojo.ResponseJsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2021/1/30 13:14
 * @UpdateUser: 寒夜
 * @UpdateDate: 2021/1/30 13:14
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Api(value = "管理平台-->注册登录相关接口", tags = "管理平台-->注册登录相关接口")
@RequestMapping("manager/passport-api/v1")
@FeignClient("manager-service")
public interface IPassportService {

    //管理平台，注册系统用户
    @ApiOperation(value = "注册API", notes = "注册API", httpMethod = "POST")
    @PostMapping("/regist")
    public ResponseJsonResult registSystemManagerUser(@RequestBody SystemManagerUserBO systemManagerUserBO);


    //管理平台，登录API
    @ApiOperation(value = "登录API ", notes = "登录API", httpMethod = "GET")
    @GetMapping("/login")
    public ResponseJsonResult loginSystemManagerUser(@ApiParam(value = "用户名", name = "userName", required = true) @RequestParam("userName") String userName,
                                                     @ApiParam(value = "密码", name = "password", required = true) @RequestParam("password") String password,
                                                     @ApiParam(value = "平台渠道", name = "platformChannel", required = true) @RequestParam("platformChannel") Integer platformChannel) throws Exception;


    //退出
    @ApiOperation(value = "退出API", notes = "退出API", httpMethod = "POST")
    @PostMapping("/signOut")
    public ResponseJsonResult signOut(@ApiParam(value = "用户id", name = "userId", required = true) @RequestParam("userId") String userId);

}
