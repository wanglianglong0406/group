package com.hy.lottery.model.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/11/27 17:34
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/11/27 17:34
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@ApiModel(value = "投注记录实体BO", description = "前端传入对象封装再次Bean中")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BetRecordBO {
    /**
     * 期彩代号
     */
    @ApiModelProperty(value = "期彩代号", name = "period", dataType = "期彩代号", required = true)
    private String period;
    /**
     * 选择球;加入球的颜色
     * 1：红球
     * 2：绿球
     * 3：紫罗兰
     */
    @ApiModelProperty(value = "选择球;加入球的颜色", name = "selectColour", dataType = "String", required = true)
    private String selectColour;
    /**
     * 奇偶数字;选择的奇偶数字
     */
    @ApiModelProperty(value = "选择的奇偶数字,传具体的值即可", name = "number", dataType = "String", required = true)
    private String number;
    /**
     * 类型代码;类型代码
     */
    @ApiModelProperty(value = "类型 PARITY  SAPRE  BCONE  EMEND", name = "type", dataType = "String", required = true)
    private String type;
    /**
     * 投注金额;投注金额
     */
    @ApiModelProperty(value = "投注金额 ", name = "betAmount", dataType = "Long", required = true)
    private Double betAmount;

}
