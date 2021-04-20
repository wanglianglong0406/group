package com.hy.enums;

/**
 * @desc 用户状态
 */
public enum UserStatus {
    NORMAL(0, "正常"),
    WRITE_OFF(1, "注销"),
    ON_LINE(2, "在线"),
    OFF_LINE(3, "离线");


    public final Integer id;
    public final String value;

    UserStatus(Integer id, String value) {
        this.id = id;
        this.value = value;
    }
}
