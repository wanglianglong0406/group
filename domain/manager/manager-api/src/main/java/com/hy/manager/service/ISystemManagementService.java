package com.hy.manager.service;

import com.hy.manager.model.bo.AnnouncementBO;
import com.hy.manager.model.bo.VipInfoBO;
import com.hy.pojo.ResponseJsonResult;
import io.swagger.annotations.*;
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
@Api(value = "管理平台-->系统管理相关接口", tags = "管理平台-->系统管理相关接口")
@RequestMapping("manager/sys-management/v1")
@FeignClient("manager-service")
public interface ISystemManagementService {

    //设置用户登录密码
    @ApiOperation(value = "设置登录密码API", notes = "设置登录密码API", httpMethod = "POST")
    @PostMapping("/setPassword")
    public ResponseJsonResult setPassword(@ApiParam(value = "用户id", name = "userId", required = true) @RequestParam("userId") String userId,
                                          @ApiParam(value = "密码", name = "password", required = true) @RequestParam("password") String password,
                                          @ApiParam(value = "确认密码", name = "confirmPassword", required = true) @RequestParam("confirmPassword") String confirmPassword);

    //查询VIP信息列表
    @ApiOperation(value = "幸运商店->>查询VIP信息列表API", notes = "查询VIP信息列表API", httpMethod = "GET")
    @GetMapping("/queryVipInfoList")
    public ResponseJsonResult queryVipInfoList(@ApiParam(value = "用户id", name = "userId", required = true) @RequestParam("userId") String userId);

    //保存VIP信息SPI
    @ApiOperation(value = "幸运商店->>保存VIP信息API", notes = "保存VIP信息API", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String", required = true)
    })
    @PostMapping("/saveVipInfo")
    public ResponseJsonResult saveVipInfo(@RequestParam("userId") String userId, @RequestBody VipInfoBO vipInfoBO);

    //修改VIP信息SPI
    @ApiOperation(value = "幸运商店->>修改VIP信息API", notes = "修改VIP信息API", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String", required = true)
    })
    @PostMapping("/editVipInfo")
    public ResponseJsonResult editVipInfo(@RequestParam("userId") String userId, @RequestBody VipInfoBO vipInfoBO);

    //查询价格信息列表
    @ApiOperation(value = "幸运商店->>查询价格信息列表API", notes = "查询价格信息列表API", httpMethod = "GET")
    @GetMapping("/queryPriceInfoList")
    public ResponseJsonResult queryPriceInfoList(@ApiParam(value = "用户id", name = "userId", required = true) @RequestParam("userId") String userId);


    //新增价格信息
    @ApiOperation(value = "幸运商店->>新增价格信息API", notes = "幸运商店->>新增价格信息API", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "String", required = true),
            @ApiImplicitParam(name = "price", value = "价格", dataType = "Double", required = true)
    })
    @PostMapping("/addPriceInfo")
    public ResponseJsonResult addPriceInfo(@RequestParam("userId") String userId, @RequestParam("price") Double price);

    //修改价格信息
    @ApiOperation(value = "幸运商店->>修改价格信息API", notes = "幸运商店->>修改价格信息API", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "String", required = true),
            @ApiImplicitParam(name = "id", value = "价格信息ID", dataType = "Integer", required = true),
            @ApiImplicitParam(name = "price", value = "价格", dataType = "Double", required = true)
    })
    @PostMapping("/editPriceInfo")
    public ResponseJsonResult editPriceInfo(@RequestParam("userId") String userId, @RequestParam("id") Integer id, @RequestParam("price") Double price);


    //查询金融产品信息列表
    @ApiOperation(value = "幸运商店->>查询金融产品信息列表API", notes = "幸运商店->>查询金融产品信息列表API", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "String", required = true)
    })
    @GetMapping("/queryFinancialProductsList")
    public ResponseJsonResult queryFinancialProductsList(@RequestParam("userId") String userId);

    //新增理财产品信息SPI
    @ApiOperation(value = "幸运商店->>新增理财产品信息API", notes = "幸运商店->>新增理财产品信息API", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "String", required = true),
            @ApiImplicitParam(name = "cycle", value = "周期", dataType = "Integer", required = true),
            @ApiImplicitParam(name = "interest", value = "利息", dataType = "Double", required = true),
            @ApiImplicitParam(name = "totalRevenue", value = "总收入", dataType = "Double", required = true)
    })
    @PostMapping("/saveFinancialProductsInfo")
    public ResponseJsonResult saveFinancialProductsInfo(@RequestParam("userId") String userId,
                                                        @RequestParam("cycle") Integer cycle,
                                                        @RequestParam("interest") Double interest,
                                                        @RequestParam("totalRevenue") Double totalRevenue);


    //修改理财产品信息SPI
    @ApiOperation(value = "幸运商店->>修改理财产品信息API", notes = "幸运商店->>修改理财产品信息API", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "String", required = true),
            @ApiImplicitParam(name = "id", value = "id", dataType = "Integer", required = true),
            @ApiImplicitParam(name = "cycle", value = "周期", dataType = "Integer", required = true),
            @ApiImplicitParam(name = "interest", value = "利息", dataType = "Double", required = true),
            @ApiImplicitParam(name = "totalRevenue", value = "总收入", dataType = "Double", required = true)
    })
    @PostMapping("/editFinancialProductsInfo")
    public ResponseJsonResult editFinancialProductsInfo(@RequestParam("userId") String userId,
                                                        @RequestParam("id") Integer id,
                                                        @RequestParam("cycle") Integer cycle,
                                                        @RequestParam("interest") Double interest,
                                                        @RequestParam("totalRevenue") Double totalRevenue);


    //获取公告列表
    @ApiOperation(value = "幸运商店->>获取公告列表API", notes = "幸运商店->>获取公告列表API", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "String", required = true)
    })
    @GetMapping("/queryAnnouncementInfoList")
    public ResponseJsonResult queryAnnouncementInfoList(@RequestParam("userId") String userId);

    @ApiOperation(value = "幸运商店->>添加公告信息API", notes = "幸运商店->>添加公告信息API", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "String", required = true)
    })
    @PostMapping("/addAnnouncementInfo")
    public ResponseJsonResult addAnnouncementInfo(@RequestParam("userId") String userId, @RequestBody AnnouncementBO announcementBO);

    @ApiOperation(value = "幸运商店->>编辑公告信息API", notes = "幸运商店->>编辑公告信息API", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "String", required = true)
    })
    @PostMapping("/editAnnouncementInfo")
    public ResponseJsonResult editAnnouncementInfo(@RequestParam("userId") String userId, @RequestBody AnnouncementBO announcementBO);

    @ApiOperation(value = "幸运商店->>删除公告信息API", notes = "幸运商店->>删除公告信息API", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "String", required = true),
            @ApiImplicitParam(name = "id", value = "公告ID", dataType = "String", required = true)
    })
    @PostMapping("/deleteAnnouncementInfo")
    public ResponseJsonResult deleteAnnouncementInfo(@RequestParam("userId") String userId, @RequestParam("id") String id);

}
