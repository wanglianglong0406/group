package com.hy.enums;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/11/22 11:35
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/11/22 11:35
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
public enum UserLevel {

    SYSTEM_ADMINISTRATOR(0, "系统管理员"),
    SYSTEM_USER(1, "系统用户"),
    GENERAL_USER(2, "普通用户");

    public final Integer id;
    public final String value;

    UserLevel(Integer id, String value) {
        this.id = id;
        this.value = value;
    }
}
