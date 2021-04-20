package com.hy.account.service;

import com.hy.account.model.bo.BankCardInfoBO;
import com.hy.pojo.ResponseJsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/12/30 14:22
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/12/30 14:22
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Api(value = "银行卡信息相关接口", tags = "银行卡信息相关接口")
@RequestMapping("luckshop/account-api/bank-info/v1")
@FeignClient("luck-shop-account-service")
public interface IBankcardInfoService {


    @ApiOperation(value = "绑定银行卡API", notes = "绑定银行卡API", httpMethod = "POST")
    @PostMapping("/bindingBankCardInfo")
    public ResponseJsonResult bindingBankCardInfo(@RequestBody BankCardInfoBO bankCardInfoBO);


    @ApiOperation(value = "我的银行卡列表信息API", notes = "我的银行卡列表信息API", httpMethod = "GET")
    @GetMapping("/myBankcardInfos")
    public ResponseJsonResult myBankcardInfos(@ApiParam(value = "用户id",name = "user_Id",type = "String",required = true) @RequestParam(value = "userId") String userId);

}
