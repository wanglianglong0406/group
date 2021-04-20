package com.hy.enums;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/12/24 17:41
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/12/24 17:41
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
public enum PayMethod {

    //UPI支付
    UPI_PAY(1, "UPIPay"),
    //账户支付
    ACCOUNT_PAY(2, "AccountPay"),
    //平台支付
    PLATFROM_PAY(3, "PlatfromPay"),
    //转账
    TRANSFER_PAY(4, "TransferPay"),
    //系统
    SYSTEM_PAY(5, "SystemPay");

    public final Integer id;
    public final String name;

    PayMethod(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
