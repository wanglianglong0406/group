package com.hy.order.model.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 订单表(Orders)实体类
 *
 * @author makejava
 * @since 2020-11-18 16:27:26
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderBO implements Serializable {
    private static final long serialVersionUID = -3607594677863429010L;
    /**
     * 用户id;用户id
     */
    private String userId;

    /**
     * 用户名称
     */
    private String userName;
    /**
     * 银行名称
     */
    private String bankName;
    /**
     * 交易金额;交易金额
     */
    private Double amount;
    /**
     * 实际支付金额;实际支付金额
     */
    private Double realPayAmount;
    /**
     * 折扣率;单位百分比1/1
     */
    private Integer discountRate;
    /**
     * 支付方式 1:UPI 2:账户余额,3:签到返佣 4 代理返佣 5 系统
     */
    private Integer payMethod;
    /**
     * 订单类型1: 投注 2 充值 3 提现 4 签到奖励 5 代理佣金 6 升级奖励 7 月票红包 8 邀请奖励 9 首次存款奖励 10 被邀请者首笔存款返利
     */
    private Integer orderType;
    //订单状态名称
    private String orderStatusName;

    //平台id
    private Integer platformId;
    //平台名称
    private String platformName;

    //游戏名称
    private String gameName;

    //期数
    private String period;
}