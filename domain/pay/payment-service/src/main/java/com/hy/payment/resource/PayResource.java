package com.hy.payment.resource;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2021/1/19 13:20
 * @UpdateUser: 寒夜
 * @UpdateDate: 2021/1/19 13:20
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix="upi.pay")
@PropertySource("classpath:pay.properties")
public class PayResource {

    private String payMemberid;
    private String payBankCode;
    private String key;
    private String paymentUrl;
    private String payNotifyUrl;
    private String payCallbackUrl;
    private String queryTradeUrl;
    private String withdrawalUrl;
    private String queryTradeWithdrawalUrl;
    private String queryBankCodeUrl;
    private String currency;
    private String withdrawalCallbackUrl;
}
