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
 * @CreateDate: 2020/12/22 16:33
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/12/22 16:33
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "投注记录对象VO", description = "返回参数VO")
public class BettingOrderRecordVO implements Serializable {
    private static final long serialVersionUID = -145390509056015628L;


    //订单号
    @ApiModelProperty(value = "订单id", name = "orderId", dataType = "String", required = true)
    private Long orderId;
    //交易金额
    @ApiModelProperty(value = "订单状态", name = "realPayAmount", dataType = "Double", required = true)
    private Double realPayAmount;
    //游戏名称
    @ApiModelProperty(value = "游戏名称", name = "gameName", dataType = "String", required = true)
    private String gameName;
    //期数
    @ApiModelProperty(value = "期数", name = "period", dataType = "String", required = true)
    private String period;
    //是否幸运装中奖
    @ApiModelProperty(value = "是否幸运装中奖", name = "isLuck", dataType = "Integer", required = true)
    private Integer isLuck;
    //输赢金额
    @ApiModelProperty(value = "输赢金额", name = "winOrLosse", dataType = "Double", required = true)
    private Double winOrLosse;
    //平台名称
    @ApiModelProperty(value = "平台名称", name = "platformName", dataType = "String", required = true)
    private String platformName;
    @ApiModelProperty(value = "交易时间", name = "time", dataType = "obj", required = true)
    private Date time;


}
