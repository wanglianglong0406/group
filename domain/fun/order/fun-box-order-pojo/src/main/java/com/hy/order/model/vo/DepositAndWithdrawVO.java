package com.hy.order.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/12/26 13:04
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/12/26 13:04
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@ApiModel(value = "充值提现对象VO", description = "返回参数VO")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepositAndWithdrawVO implements Serializable {
    private static final long serialVersionUID = 5663040862383584707L;

    /**
     * 订单主键;同时也是订单编号
     */
    @ApiModelProperty(value = "订单ID", name = "orderId", dataType = "Long", required = true)
    private Long orderId;
    /**
     * 用户id;用户id
     */
    @ApiModelProperty(value = "用户ID", name = "userId", dataType = "String",required = true)
    private String userId;

    /**
     * 用户名称
     */
    @ApiModelProperty(value = "用户名", name = "userName", dataType = "String", required = true)
    private String userName;
    /**
     * 银行名称
     */
    @ApiModelProperty(value = "银行名称", name = "bankName", dataType = "String", required = true)
    private String bankName;
    /**
     * 交易金额;交易金额
     */
    @ApiModelProperty(value = "交易金额", name = "amount", dataType = "Double", required = true)
    private Double amount;
    /**
     * 实际支付金额;实际支付金额
     */
    @ApiModelProperty(value = "收到金额", name = "receivedAmount", dataType = "Double", required = true)
    private Double receivedAmount;

    /**
     * 支付方式;
     */
    @ApiModelProperty(value = "支付方式ID", name = "payMethod", dataType = "Integer", required = true)
    private Integer payMethod;

    /**
     * 支付方式名称
     */
    @ApiModelProperty(value = "支付方式名称", name = "payMethodName", dataType = "String", required = true)
    private String payMethodName;

    /**
     * 订单状态
     */
    @ApiModelProperty(value = "订单状态", name = "orderStatusName", dataType = "String", required = true)
    private String orderStatusName;


    /**
     * 创建时间;对应[10:待付款]状态
     */
    @ApiModelProperty(value = "交易时间", name = "createTime", dataType = "obj", required = true)
    private Date createTime;

    /**
     * 扩展字段
     */
    @ApiModelProperty(value = "详细信息", name = "detail", dataType = "String", required = true)
    private String detail;
}
