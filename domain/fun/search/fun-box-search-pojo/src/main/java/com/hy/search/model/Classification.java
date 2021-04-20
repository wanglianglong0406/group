package com.hy.search.model;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 首页分类(Classification)表实体类
 *
 * @author 寒夜
 * @since 2020-11-18 13:33:05
 */
@SuppressWarnings("serial")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Classification extends Model<Classification> {
    //主键 分类id主键
    private Integer id;
    //分类名称 分类名称
    private String name;
    //分类类型 分类得类型，
    //1:一级大分类
    //2:二级分类
    //3:三级小分类
    private Integer type;
    //父id 父id 上一级依赖的id，1级分类则为0，二级三级分别依赖上一级
    private Integer fatherId;
    //图标 logo
    private String logo;
    //口号
    private String slogan;
    //分类图
    private String catImage;
    //背景颜色
    private String bgColor;



}