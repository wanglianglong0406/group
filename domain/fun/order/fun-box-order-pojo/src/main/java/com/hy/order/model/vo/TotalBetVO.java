package com.hy.order.model.vo;

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
 * @CreateDate: 2020/12/22 15:23
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/12/22 15:23
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "投注流水统计对象VO", description = "返回参数VO")
public class TotalBetVO implements Serializable {

    private static final long serialVersionUID = 4260460496931621476L;
    @ApiModelProperty(value = "总金额", name = "totalBets", dataType = "double", required = true)
    private double totalBets;
    @ApiModelProperty(value = "输掉总金额", name = "totalWinsLosses", dataType = "double", required = true)
    private double totalWinsLosses;

}
