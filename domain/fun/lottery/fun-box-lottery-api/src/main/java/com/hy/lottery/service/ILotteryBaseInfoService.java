package com.hy.lottery.service;

import com.hy.pojo.ResponseJsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/11/24 17:10
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/11/24 17:10
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Api(value = "期货彩票基本信息相关API", tags = "期货彩票基本信息相关API")
@RequestMapping("lottery-api/base-info/v1")
@FeignClient("fun-box-lottery-service")
public interface ILotteryBaseInfoService {
    @ApiOperation(value = "获取期货彩票基本信息列表", notes = "获取期货彩票基本信息列表", httpMethod = "GET")
    @GetMapping("baseInfo")
    public ResponseJsonResult queryLotteryBasicInfo();
}
