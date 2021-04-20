package com.hy.center.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: $- 商品信息 -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2021/1/5 13:45
 * @UpdateUser: 寒夜
 * @UpdateDate: 2021/1/5 13:45
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemsVO implements Serializable {

    /**
     * 商品主键id;唯一主键（商品ID）
     */

    private String id;
    /**
     * 商品名称;商品名称
     */
    private String itemName;
    /**
     * 上下架状态;上下架状态,1:上架 2:下架
     */
    private Integer onOffStatus;
    /**
     * 商品内容;商品内容
     */
    private String content;
    /**
     * 图片地址;图片地址
     */
    private String url;
    /**
     * 商品价格;商品价格
     */
    private Double price;
    /**
     * 创建时间;创建时间
     */
    private String createTime;
}
