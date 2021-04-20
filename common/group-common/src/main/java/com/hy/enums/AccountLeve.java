package com.hy.enums;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/11/19 16:36
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/11/19 16:36
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
public enum AccountLeve {

    ONE(0, "主账户"),
    TWO(1, "二级账户");
    public final Integer id;
    public final String value;

    AccountLeve(Integer id, String value) {
        this.id = id;
        this.value = value;
    }
}
