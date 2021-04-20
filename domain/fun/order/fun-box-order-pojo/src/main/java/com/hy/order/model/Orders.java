package com.hy.order.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

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
public class Orders extends Model<Orders> implements Serializable {
    private static final long serialVersionUID = -2422869549291208092L;
    /**
     * 订单主键;同时也是订单编号
     */
    @TableId(type = IdType.ID_WORKER)
    private Long orderId;
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
     * 支付方式;支付方式 1:UPI 2：账户余额
     */
    private Integer payMethod;
    /**
     * 订单状态;10：待付款  20：已付款   30  交易成功  40：交易关闭（待付款时，用户取消 或 长时间未付款，系统识别后自动关闭）
     */
    private Integer orderStatus;

    //订单状态名称
    private String orderStatusName;
    /**
     * 订单类型;订单类型 : 1: 投注 2 充值 3 提现 4 签到奖励 5 代理佣金
     */
    private Integer orderType;
    //平台id
    private Integer platformId;
    //平台名称
    private String platformName;
    //游戏名称
    private String gameName;

    //期数
    private String period;
    //是否幸运装中奖
    private Integer isLuck;
    //输赢金额
    private Double winOrLosse;
    /**
     * 扩展字段
     */
    private String extand;
    /**
     * 逻辑删除状态;1: 删除 0:未删除
     */
    private Integer isDelete;
    /**
     * 创建时间;对应[10:待付款]状态
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

    //存款标致  1  第一笔存款   2 第二笔存款   3 后续存款
    private Integer isRechargeFlag;

}