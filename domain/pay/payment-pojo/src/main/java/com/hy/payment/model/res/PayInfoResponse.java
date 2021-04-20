package com.hy.payment.model.res;

import lombok.*;

import java.io.Serializable;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2021/1/17 14:30
 * @UpdateUser: 寒夜
 * @UpdateDate: 2021/1/17 14:30
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PayInfoResponse implements Serializable {
    private static final long serialVersionUID = -6121608029495699860L;
    private String payUrl;
    private String resCode;
    private String msg;
}
