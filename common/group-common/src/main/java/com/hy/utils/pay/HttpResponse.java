package com.hy.utils.pay;

import lombok.*;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2021/1/16 22:10
 * @UpdateUser: 寒夜
 * @UpdateDate: 2021/1/16 22:10
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Data
@Builder
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class HttpResponse {
    private int statusCode;

    private String body;
}
