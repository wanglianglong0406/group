package com.hy.payment.model.res;

import lombok.*;

import java.io.Serializable;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2021/1/19 15:31
 * @UpdateUser: 寒夜
 * @UpdateDate: 2021/1/19 15:31
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BankCodeResponse implements Serializable {

    private static final long serialVersionUID = -6336692270117243327L;
    private String status;
    private String msg;
    private Object data;
}
