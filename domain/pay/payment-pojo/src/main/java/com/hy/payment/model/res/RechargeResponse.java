package com.hy.payment.model.res;

import lombok.*;

import java.io.Serializable;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2021/1/17 14:20
 * @UpdateUser: 寒夜
 * @UpdateDate: 2021/1/17 14:20
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RechargeResponse implements Serializable {

    private static final long serialVersionUID = -3232973481181939747L;
    private String status;
    private String msg;
    private DataBody data;
}
