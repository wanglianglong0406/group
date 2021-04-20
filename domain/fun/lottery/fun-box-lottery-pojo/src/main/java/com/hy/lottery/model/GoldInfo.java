package com.hy.lottery.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/11/24 17:27
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/11/24 17:27
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoldInfo extends Model<GoldInfo> {
    private static final long serialVersionUID = -4301458298701360192L;
    /** ID（唯一主键）;唯一主键 */
    @TableId(type= IdType.ID_WORKER)
    private Long id ;
    /** 黄金规格;黄金规格 */
    private String type ;
    /** 价格;价格 */
    private Long price ;
    /** 浮动数值;浮动数值 */
    private Integer number ;
    /** 上下架状态;上下架状态
     1  ： 上架
     2 ： 下架 */
    private Integer goldStatus ;
    /** 创建时间;创建时间 */
    private Date createTime ;
    /** 更新时间;更新时间 */
    private Date updatedTime ;
}
