package com.hy.user.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: $- 反馈信息表 -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/12/18 21:16
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/12/18 21:16
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@ApiModel(value = "反馈信息", description = "反馈信息")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackInfo extends Model<FeedbackInfo> implements Serializable {
    private static final long serialVersionUID = -7392138504046929104L;

    /** ID（唯一主键）;唯一主键 */
    @ApiModelProperty(value = "唯一主键 反馈消息ID", name = "id", dataType = "Long", required = true)
    @TableId(type= IdType.ID_WORKER)
    private Long id ;
    /** 用户ID;用户ID */
    @ApiModelProperty(value = "用户ID", name = "userId", dataType = "String", required = true)
    private String userId ;
    /** 问题描述;问题描述 */
    @ApiModelProperty(value = "问题描述", name = "des", dataType = "String", required = true)
    private String des ;
    /** 问题类型;问题类型 */
    @ApiModelProperty(value = "问题类型", name = "type", dataType = "Integer", required = true)
    private Integer type ;
    /** 问题类型名称;问题类型名称 */
    @ApiModelProperty(value = "问题类型名称", name = "name", dataType = "String", required = true)
    private String name ;
    /** 图片描述1;图片描述1 */
    @ApiModelProperty(value = "图片地址1", name = "imageUrl", dataType = "String", required = true)
    private String imageUrl ;
    /** 是否已处理;是否已处理（1 未处理  2 已处理） */
    @ApiModelProperty(value = "是否已处理（1 未处理  2 已处理）", name = "isFlag", dataType = "Integer", required = true)
    private Integer isFlag ;
    /** 创建时间;创建时间 */
    @ApiModelProperty(value = "创建时间", name = "createTime", dataType = "Date", required = true)
    private Date createTime ;
    /** 更新时间;更新时间 */
    @ApiModelProperty(value = "更新时间", name = "updateTime", dataType = "Date", required = true)
    private Date updateTime ;
}
