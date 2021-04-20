package com.hy.payment.model.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2021/1/19 14:06
 * @UpdateUser: 寒夜
 * @UpdateDate: 2021/1/19 14:06
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WithdrawalReq implements Serializable {

    private static final long serialVersionUID = -7682276630020947547L;
    String outTradeNo;
    String money;
    String bankname;
    String subbranch;
    String accountname;
    String cardnumber;
    String province;
    String city;
    String ifsc;
}
