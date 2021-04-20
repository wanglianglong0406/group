package com.hy.payment.model.res;

import lombok.*;

import java.io.Serializable;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2021/1/18 15:20
 * @UpdateUser: 寒夜
 * @UpdateDate: 2021/1/18 15:20
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TradeInfoResponse implements Serializable {


    private static final long serialVersionUID = -353926146879724439L;
    //商户编号
    private String memberid;
    //订单号
    private String orderid;
    //订单金额
    private String amount;
    //支付成功时间
    private String time_end;
    //交易流水号
    private String transaction_id;
    //返回码
    private String returncode;
    //交易状态
    private String trade_state;
    //签名
    private String sign;
}
