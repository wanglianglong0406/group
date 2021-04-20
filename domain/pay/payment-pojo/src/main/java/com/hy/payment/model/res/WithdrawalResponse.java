package com.hy.payment.model.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2021/1/19 14:10
 * @UpdateUser: 寒夜
 * @UpdateDate: 2021/1/19 14:10
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WithdrawalResponse {
    //状态
    private String status;
    //状态描述
    private String msg;
    //平台流水号
    private String transaction_id;
}
