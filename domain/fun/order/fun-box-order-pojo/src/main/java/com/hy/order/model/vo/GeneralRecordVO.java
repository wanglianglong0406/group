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
 * @CreateDate: 2020/12/24 12:42
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/12/24 12:42
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@ApiModel(value = "一般记录对象VO", description = "返回参数VO")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GeneralRecordVO implements Serializable {
    private static final long serialVersionUID = -4101444833748739466L;

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
     * 标题
     */
    @ApiModelProperty(value = "标题", name = "title", dataType = "String", required = true)
    private String title;

    /**
     * 订单类型;
     */
    @ApiModelProperty(value = "订单类型", name = "orderType", dataType = "Integer", required = true)
    private Integer orderType;

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
