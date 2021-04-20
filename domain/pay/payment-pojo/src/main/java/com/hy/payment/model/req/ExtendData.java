package com.hy.payment.model.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2021/1/19 13:56
 * @UpdateUser: 寒夜
 * @UpdateDate: 2021/1/19 13:56
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExtendData implements Serializable {

    private static final long serialVersionUID = -1321447818677358437L;
    private String ifscCode;
}
