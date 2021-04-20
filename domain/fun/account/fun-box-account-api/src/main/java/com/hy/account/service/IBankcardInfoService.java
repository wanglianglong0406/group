package com.hy.account.service;

import com.hy.account.model.bo.BankCardInfoBO;
import com.hy.pojo.ResponseJsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @Description: $- 银行卡信息表 银行卡信息表(BankcardInfo)表服务接口 -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/11/20 13:59
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/11/20 13:59
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Api(value = "银行卡相关接口", tags = "银行卡相关接口")
@RequestMapping("bankinfo-api/bankinfo/v1")
@FeignClient("fun-box-account-service")
public interface IBankcardInfoService {

    @ApiOperation(value = "绑定银行卡API", notes = "绑定银行卡API", httpMethod = "POST")
    @PostMapping("/bindingBankCardInfo")
    public ResponseJsonResult bindingBankCardInfo(@RequestBody BankCardInfoBO bankCardInfoBO);


    @ApiOperation(value = "银行卡列表信息API", notes = "银行卡列表信息API", httpMethod = "GET")
    @GetMapping("/bankCardInfos")
    public ResponseJsonResult getBankcardInfos(@ApiParam(value = "用户id",name = "user_Id",type = "String",required = true) @RequestParam(value = "userId") String userId,
                                               @ApiParam(value = "账户id",name = "accountId",type = "Long",required = true) @RequestParam(value = "accountId")  Long accountId);
}
