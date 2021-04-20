package com.hy.lottery.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Description: $- 彩票基本信息表-$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/11/24 17:05
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/11/24 17:05
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LotteryBaseInfo extends Model<LotteryBaseInfo> {
    /** ID（唯一主键）;唯一主键 */
    @TableId(type= IdType.ID_WORKER)
    @JsonIgnore
    private Long id ;
    /** 类型代码;类型代码 */
    private String type ;
    /** 标题;标题 */
    private String title ;
    /** 图标;图标 */
    private String avatar ;
    /** 创建时间;创建时间 */
    @JsonIgnore
    private Date createTime ;
    /** 更新时间;更新时间 */
    @JsonIgnore
    private Date updatedTime ;
}
