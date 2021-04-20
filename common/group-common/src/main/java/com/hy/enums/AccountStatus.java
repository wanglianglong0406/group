package com.hy.enums;

/**
 * @Description: $- 钱包账户状态 -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/11/19 16:32
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/11/19 16:32
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
public enum AccountStatus {

    NORMAL(0, "正常"),
    CANCELLATION(1, "注销"),
    FROZEN(2, "冻结"),
    OPENING(3, "开户中");


    public final Integer type;
    public final String value;

    AccountStatus(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
