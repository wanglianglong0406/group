package com.hy.payment.model.res;

import lombok.*;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2021/1/19 15:13
 * @UpdateUser: 寒夜
 * @UpdateDate: 2021/1/19 15:13
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WithdrawalTradeInfoResponse {
    //状态
    private String status;
    //状态描述
    private String msg;
}
