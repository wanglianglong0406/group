package com.hy.search.service;

import com.hy.pojo.ResponseJsonResult;
import com.hy.search.model.Vipinfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/12/13 18:30
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/12/13 18:30
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */

@Api(value = "VIP贵宾相关接口", tags = "VIP贵宾相关接口")
@RequestMapping("search-api/vip/v1")
@FeignClient("fun-box-search-service")
public interface IVipService {
    //查询VIP信息列表
    @ApiOperation(value = "查询VIP信息列表API", notes = "查询VIP信息列表API", httpMethod = "GET")
    @GetMapping("/vipInfoList")
    public ResponseJsonResult queryVipInfoList();

    //查询VIP信息
    @ApiOperation(value = "查询VIP信息API", notes = "查询VIP信息API", httpMethod = "GET", hidden = true)
    @GetMapping("/vipInfo")
    public Vipinfo queryVipInfo(@ApiParam(value = "VIP等级", name = "vipLevel", required = true, example = "0")
                                @RequestParam(value = "vipLevel") Integer vipLevel);

    //查询VIP信息详情
    @ApiOperation(value = "查询VIP信息详情API", notes = "查询VIP信息详情API", httpMethod = "GET")
    @GetMapping("/vipInfoDetailByVipLevel")
    public ResponseJsonResult queryVipInfoDetailByVipLevel(@ApiParam(value = "VIP等级", name = "vipLevel", required = true, example = "0")
                                                           @RequestParam(value = "vipLevel") Integer vipLevel);

    //查询VIP特权信息
    @ApiOperation(value = "查询VIP特权信息API", notes = "查询VIP特权信息API", httpMethod = "GET")
    @GetMapping("/vipRateInfoList")
    public ResponseJsonResult queryVipRateInfoList();
}
