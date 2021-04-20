package com.hy.manager.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2021/1/30 15:21
 * @UpdateUser: 寒夜
 * @UpdateDate: 2021/1/30 15:21
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemsBO implements Serializable {

    private static final long serialVersionUID = 920357083524290129L;
    /**
     * 商品主键id;唯一主键（商品ID）
     */
    @ApiModelProperty(value = "唯一主键（商品ID）(编辑时必填，添加非必填)", name = "id", dataType = "Long")
    private Long id;
    /**
     * 商品名称;商品名称
     */
    @ApiModelProperty(value = "商品名称", name = "itemName", dataType = "String", required = true)
    private String itemName;
    /**
     * 上下架状态;上下架状态,1:上架 2:下架
     */
    @ApiModelProperty(value = "上下架状态,1:上架 2:下架", name = "onOffStatus", dataType = "Integer", required = true)
    private Integer onOffStatus;
    /**
     * 商品内容;商品内容
     */
    @ApiModelProperty(value = "商品内容", name = "content", dataType = "String", required = true)
    private String content;
    /**
     * 图片地址;图片地址
     */
    @ApiModelProperty(value = "图片地址", name = "url", dataType = "String", required = true)
    private String url;
    /**
     * 商品价格;商品价格
     */
    @ApiModelProperty(value = "商品价格", name = "price", dataType = "Double", required = true)
    private Double price;
}
