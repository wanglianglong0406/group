package com.hy.enums;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/12/24 17:08
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/12/24 17:08
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
public enum OrderType {
    //投注
    BETS(1, "Bets"),
    //存款
    DEPOSIT(2, "Deposit"),
    //提现
    WITHDRAW(3, "Withdraw"),
    //签到
    SIGN_IN(4, "sign in"),
    //代理佣金
    AGENCY_COMMISSION(5, "Agency commission"),
    //升级奖励
    UPGRADE_REWARD(6, "Upgrade reward"),
    //月红包
    MONTHLY_RED_ENVELOPE(7, "Monthly red envelope"),
    //邀请奖励
    INVITATION_REWARD(8, "Invitation reward"),
    //首次存款奖励
    FIRST_DEPOSIT(9, "First deposit"),
    //存款返利
    DEPOSIT_REBATE(10, "Deposit rebate"),
    //转出
    TRANSFER_OUT(11, "Transfer out"),
    //转入
    TRANSFER_IN(12, "Transfer in"),
    //理财日息结算
    DAILY_INTEREST(13, "financial DailyInterest"),
    //任务佣金
    TASK_COMMISSION(14, "Task Commission");
    public final Integer id;
    public final String name;

    OrderType(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
