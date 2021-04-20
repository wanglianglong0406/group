package com.hy.auth.service.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: $- Account -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/11/17 8:15
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/11/17 8:15
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class  Account implements Serializable {

    private String userId;

    private String token;

    private String refreshToken;
}
