package com.hy.enums;

/**
 * @Description: $- Level -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/11/18 10:46
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/11/18 10:46
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
public enum Level {
    VIP_ZERO(0, "VIP0"),
    VIP_ONE(1, "VIP1"),
    VIP_TWO(2, "VIP2"),
    VIP_THREE(3, "VIP3"),
    VIP_FOUF(4, "VIP4"),
    VIP_FIVE(5, "VIP5"),
    VIP_SIX(6, "VIP6"),
    VIP_SEVEN(7, "VIP7"),
    VIP_EIGHT(8, "VIP8"),
    VIP_NINE(9, "VIP9"),
    VIP_TEN(10, "VIP10"),
    NOT_VIP(100, "不是贵宾");
    public final Integer id;
    public final String value;

    Level(Integer id, String value) {
        this.id = id;
        this.value = value;
    }
}
