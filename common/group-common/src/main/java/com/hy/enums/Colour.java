package com.hy.enums;

/**
 * @Description: $- 颜色 -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/11/22 11:35
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/11/22 11:35
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
public enum Colour {

    RED(1, "RED"),
    GREEN(2, "GREEN"),
    VIOLET(3, "VIOLET");

    public final Integer type;
    public final String value;

    Colour(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
