package com.hy.center.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: $- 商品信息 -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2021/1/5 13:45
 * @UpdateUser: 寒夜
 * @UpdateDate: 2021/1/5 13:45
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Items extends Model<Items> implements Serializable {
    private static final long serialVersionUID = 8722671778674084613L;

    /** 商品主键id;唯一主键（商品ID） */
    @TableId(type = IdType.ID_WORKER)
    private Long id ;
    /** 商品名称;商品名称 */
    private String itemName ;
    /** 上下架状态;上下架状态,1:上架 2:下架 */
    private Integer onOffStatus ;
    /** 商品内容;商品内容 */
    private String content ;
    /** 图片地址;图片地址 */
    private String url ;
    /** 商品价格;商品价格 */
    private Double price ;
    /** 更新时间;更新时间 */
    @JsonIgnore
    private Date updateTime ;
    /** 创建时间;创建时间 */
    private Date createTime ;
}
