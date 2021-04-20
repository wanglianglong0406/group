package com.hy.utils.pay;

import lombok.*;

import java.util.Map;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2021/1/16 22:09
 * @UpdateUser: 寒夜
 * @UpdateDate: 2021/1/16 22:09
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class HttpRequestParams {
    private String url;
    private Map<String, String> params;
    private Map<String, String> heads;
}
