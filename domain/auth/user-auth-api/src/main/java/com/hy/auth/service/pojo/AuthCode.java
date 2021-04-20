package com.hy.auth.service.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description: $- AuthCode -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/11/17 8:16
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/11/17 8:16
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@AllArgsConstructor
public enum AuthCode {

    SUCCESS(1L),
    USER_NOT_FOUND(1000L),
    INVALID_CREDENTIAL(2000L);

    @Getter
    private Long code;
}
