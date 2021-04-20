package com.hy.payment.model.res;

import lombok.*;

import java.io.Serializable;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2021/1/19 15:06
 * @UpdateUser: 寒夜
 * @UpdateDate: 2021/1/19 15:06
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CallBackOfWithdrawalResponse implements Serializable {
    private static final long serialVersionUID = 1102313825465312720L;

    String status;
    String outTradeNo;
    String amount;
    String message;
    String sign;

}
