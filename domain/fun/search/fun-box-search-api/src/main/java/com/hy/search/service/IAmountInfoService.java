package com.hy.search.service;

import com.hy.pojo.ResponseJsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/12/17 15:21
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/12/17 15:21
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Api(value = "价格信息相关接口", tags = "价格信息相关接口")
@RequestMapping("search-api/price-info/v1")
@FeignClient("fun-box-search-service")
public interface IAmountInfoService {

    @ApiOperation(value = "价格信息列表API", notes = "价格信息列表API", httpMethod = "GET")
    @GetMapping("/priceInfoList")
    public ResponseJsonResult queryPriceInfoList();
}
