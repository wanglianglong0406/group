package com.hy.enums;

/**
 * @Description: $- 类型代码 -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/11/22 11:35
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/11/22 11:35
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
public enum CodeType {

    PARITY(1, "PARITY"),
    SAPRE(2, "SAPRE"),
    BCONE(3, "BCONE"),
    EMEND(3, "EMEND");

    public final Integer type;
    public final String value;

    CodeType(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
