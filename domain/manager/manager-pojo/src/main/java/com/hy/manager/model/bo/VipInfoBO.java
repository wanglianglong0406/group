package com.hy.manager.model.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2021/1/4 17:44
 * @UpdateUser: 寒夜
 * @UpdateDate: 2021/1/4 17:44
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@ApiModel(value = "系统管理平台VIP对象BO", description = "从客户端，由用户传入的数据封装在此entity中")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VipInfoBO implements Serializable {


    /**
     * VIP等级ID;VIP等级ID
     */
    @ApiModelProperty(value = "VIP等级ID", name = "vipLevel", dataType = "Integer", required = true)
    private Integer vipLevel;
    /**
     * 名称;名称
     */
    @ApiModelProperty(value = "名称", name = "name", dataType = "String", required = true)
    private String name;
    /**
     * 资产要求;资产要求
     */
    @ApiModelProperty(value = "资产要求", name = "assets", dataType = "Double", required = true)
    private Double assets;
    /**
     * 订单任务佣金比率;订单任务佣金比率
     */
    @ApiModelProperty(value = "订单任务佣金比率", name = "commissionRate", dataType = "Double", required = true)
    private Double commissionRate;
    /**
     * 月取款限额;月取款限额
     */
    @ApiModelProperty(value = "月取款限额", name = "monthlyWithdrawalLimit", dataType = "Double", required = true)
    private Double monthlyWithdrawalLimit;
    /**
     * 日取款次数;日取款次数
     */
    @ApiModelProperty(value = "日取款次数", name = "numberOfDailyWithdrawals", dataType = "Integer", required = true)
    private Integer numberOfDailyWithdrawals;
    /**
     * 任务数量;任务数量
     */
    @ApiModelProperty(value = "任务数量", name = "numberOfTasks", dataType = "Integer", required = true)
    private Integer numberOfTasks;
}
