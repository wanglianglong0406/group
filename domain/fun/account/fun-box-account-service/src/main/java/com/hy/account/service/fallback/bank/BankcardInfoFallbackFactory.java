package com.hy.account.service.fallback.bank;

import com.hy.account.model.bo.BankCardInfoBO;
import com.hy.pojo.ResponseJsonResult;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Description: $- IBankcardInfoService 服务降级处理流程-$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/11/20 15:17
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/11/20 15:17
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Slf4j
@Component
public class BankcardInfoFallbackFactory implements FallbackFactory<BankCardInfoServiceFeignClient> {
    @Override
    public BankCardInfoServiceFeignClient create(Throwable throwable) {

        return new BankCardInfoServiceFeignClient(){
            @Override
            public ResponseJsonResult bindingBankCardInfo(BankCardInfoBO bankCardInfoBO) {
                log.error("Fallback: Loading... {} ", bankCardInfoBO.toString());
                return ResponseJsonResult.errorMsg("Please wait a moment, waiting for response...");
            }

            @Override
            public ResponseJsonResult getBankcardInfos(String userId, Long accountId) {
                log.error("Fallback: Loading... 用户id : {} 账户ID : {} ", userId,accountId);
                return ResponseJsonResult.errorMsg("Please wait a moment, waiting for response...");
            }
        };


    }


}
