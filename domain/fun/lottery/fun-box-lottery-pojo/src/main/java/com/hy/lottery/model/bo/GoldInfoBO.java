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
 * @CreateDate: 2020/11/24 17:27
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/11/24 17:27
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@ApiModel(value = "黄金基础价格信息维护", description = "前端传入对象封装再次Bean中")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoldInfoBO {

    /** 黄金规格;黄金规格 */
    @ApiModelProperty(value = "类型 PARITY  SAPRE  BCONE  EMEND", name = "type", dataType = "String",required = true)
    private String type ;
    /** 价格;价格 */
    @ApiModelProperty(value = "当前黄金价格", name = "price", dataType = "Long",required = true)
    private Long price ;
    /** 浮动数值;浮动数值 */
    @ApiModelProperty(value = "抽奖浮动数值", name = "number", dataType = "Integer",required = true)
    private Integer number ;
    /** 上下架状态;上下架状态
     1  ： 上架
     2 ： 下架 */
    @ApiModelProperty(value = "上下架状态 1 上架  2 下架", name = "goldStatus", dataType = "Integer")
    private Integer goldStatus ;

}
