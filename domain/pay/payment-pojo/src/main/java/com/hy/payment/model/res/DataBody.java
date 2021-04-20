package com.hy.payment.model.res;

import lombok.*;

import java.io.Serializable;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2021/1/17 14:41
 * @UpdateUser: 寒夜
 * @UpdateDate: 2021/1/17 14:41
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DataBody implements Serializable {

    private static final long serialVersionUID = -1134101071782743577L;
    private String pay_url;

    private String pay_bankcode;
}
