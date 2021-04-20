package com.hy.account.service.fallback.bank;

import com.hy.account.model.bo.BankCardInfoBO;
import com.hy.pojo.ResponseJsonResult;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/11/21 10:48
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/11/21 10:48
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Component
// 假的requestmapping，把spring糊弄过去
@RequestMapping("JokeJoke")
@Slf4j
public class BankcardInfoFallback implements BankCardInfoServiceFeignClient{
    //实现多级降级
    @Override
    @HystrixCommand(fallbackMethod = "bindingBankCardInfo2")
    public ResponseJsonResult bindingBankCardInfo(BankCardInfoBO bankCardInfoBO) {
        log.error("Fallback: Loading... {} ", bankCardInfoBO.toString());
        return ResponseJsonResult.errorMsg("Please wait a moment, waiting for response...");

    }

    @Override
    public ResponseJsonResult getBankcardInfos(String userId, Long accountId) {
        return null;
    }

    public ResponseJsonResult bindingBankCardInfo2(BankCardInfoBO bankCardInfoBO)  {
        log.error("Fallback: Loading... {} ",bankCardInfoBO.toString());
        return ResponseJsonResult.errorMsg("Please wait a moment, waiting for response...");
    }
}
