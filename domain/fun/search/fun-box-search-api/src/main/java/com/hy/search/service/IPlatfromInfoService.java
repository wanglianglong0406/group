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
 * @CreateDate: 2020/12/22 23:18
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/12/22 23:18
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */

@Api(value = "平台信息相关接口", tags = "平台信息相关接口")
@RequestMapping("search-api/platfrom-info/v1")
@FeignClient("fun-box-search-service")
public interface IPlatfromInfoService {

    //查询平台信息
    @ApiOperation(value = "平台信息列表API", notes = "平台信息列表API", httpMethod = "GET")
    @GetMapping("/platfromInfoList")
    public ResponseJsonResult queryPlatfromInfoList();

}
