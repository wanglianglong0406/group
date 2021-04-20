package com.hy.search.service;

import com.hy.search.model.PayMethod;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/12/24 13:07
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/12/24 13:07
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Api(value = "支付方式相关接口", tags = "支付方式相关接口")
@RequestMapping("search-api/pay-method/v1")
@FeignClient("fun-box-search-service")
public interface IPayMethodService {

    //查询支付方式列表
    @ApiOperation(value = "支付方式信息列表API", notes = "支付方式信息列表API", httpMethod = "GET",hidden = true)
    @GetMapping("/platfromInfoList")
    public List<PayMethod> queryPayMethodInfoList();
}
