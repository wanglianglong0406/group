package com.hy.enums;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/12/22 14:46
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/12/22 14:46
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
public enum Platfrom {

    KingMaker(1, "KingMaker"),
    GG(2, "GG Fishing"),
    AE(3, "AE Sexy"),
    HB(4, "HB Game"),
    IBC(5, "IBC Sports"),
    DS(6, "DS Chess"),
    MG(7, "MG Game"),
    WM(8, "WM Live"),
    UPG(9, "UPG Game"),
    DG(10, "DG Live"),
    Futures(11, "Futures"),
    S128(12, "S128 CockFight"),
    SYSTEM_SERVICE(13, "SYSTEM SERVICE");

    public final Integer id;
    public final String name;

    Platfrom(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
