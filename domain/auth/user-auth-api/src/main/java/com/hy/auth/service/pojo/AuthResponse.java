package com.hy.auth.service.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: $- AuthResponse -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/11/17 8:17
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/11/17 8:17
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private Account account;

    private Long code;
}
