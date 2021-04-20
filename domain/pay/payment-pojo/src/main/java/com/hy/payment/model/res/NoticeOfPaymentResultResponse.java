package com.hy.payment.model.res;

import lombok.*;

import java.io.Serializable;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2021/1/18 15:39
 * @UpdateUser: 寒夜
 * @UpdateDate: 2021/1/18 15:39
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NoticeOfPaymentResultResponse implements Serializable {

    private static final long serialVersionUID = -7063409821182612604L;
    //商户编号
    private String memberId;
    //订单号
    private String orderId;
    //订单金额
    private String amount;
    //交易流水号
    private String transactionId;
    //交易时间
    private String datetime;
    //返回码
    private String returnCode;
    //扩展返回
    private String attach;
    //签名
    private String sign;
}
