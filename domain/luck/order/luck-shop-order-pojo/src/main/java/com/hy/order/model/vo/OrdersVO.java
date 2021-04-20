package com.hy.order.model.vo;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/12/30 17:16
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/12/30 17:16
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrdersVO implements Serializable {

    private static final long serialVersionUID = -5065236904335094600L;
    /**
     * 订单主键;同时也是订单编号
     */
    private String orderId;
    /**
     * 用户id;用户id
     */
    private String userId;
    /**
     * 用户名称;用户名称
     */
    private String userName;
    /**
     * 银行名称;银行名称
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
    /** 支付方式;支付方式 1:UPI 2：账户余额 */
    /**
     * 佣金比率;佣金比率
     */
    private Double commissionRate;
    /**
     * 佣金奖励金额;佣金奖励金额
     */
    private Double commissionRewarsAmount;
    /**
     * 返佣金额;返佣金额
     */
    private Double commissionRefundAmount;
    /**
     * 商品图片连接;商品图片连接
     */
    private String imgUrl;
    /**
     * 支付方式
     */
    private Integer payMethod;
    /**
     * 订单状态;10：待付款  20：已付款   30  交易成功  40：交易关闭（待付款时，用户取消 或 长时间未付款，系统识别后自动关闭）
     */
    private Integer orderStatus;
    /**
     * 订单状态名称;订单状态名称
     */
    private String orderStatusName;
    /**
     * 订单类型;订单类型 : 1: 订单任务佣金 2 充值 3 提现 4 邀请朋友佣金 5 朋友充值回扣佣金
     */
    private Integer orderType;
    /**
     * 充值标识;1 第一笔  2 第二笔 3 其他
     */
    private Integer isRechargeFlag;
    /**
     * 逻辑删除状态;1: 删除 0:未删除
     */
    private Integer isDelete;
    /**
     * 扩展字段;扩展字段
     */
    private String extand;
    /**
     * 交易时间;交易时间 对应[10:待付款]状态
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 交易成功时间;对应[30：交易成功]状态
     */
    private Date successTime;
    /**
     * 支付成功时间;对应[20:已付款]状态
     */
    private Date payTime;
    /**
     * 交易关闭时间;对应[40：交易关闭]状态
     */
    private Date closeTime;
}
