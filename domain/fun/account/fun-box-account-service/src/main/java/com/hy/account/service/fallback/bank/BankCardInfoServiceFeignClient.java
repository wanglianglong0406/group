package com.hy.account.service.fallback.bank;

import com.hy.account.service.IBankcardInfoService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/11/21 10:41
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/11/21 10:41
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@FeignClient(value = "fun-box-account-service",fallbackFactory = BankcardInfoFallbackFactory.class)
public interface BankCardInfoServiceFeignClient extends IBankcardInfoService {
}
