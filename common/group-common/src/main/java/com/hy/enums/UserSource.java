package com.hy.enums;

/**
 * @Description: $- 用户来源 -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/11/22 11:35
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/11/22 11:35
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
public enum UserSource {

    one(1, "H5"),
    two(2, "APP"),
    three(3, "PC"),
    fore(4, "其他");

    public final Integer type;
    public final String value;

    UserSource(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
