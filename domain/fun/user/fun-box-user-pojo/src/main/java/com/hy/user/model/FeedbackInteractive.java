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
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/12/18 21:26
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/12/18 21:26
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@ApiModel(value = "反馈互动信息", description = "反馈互动信息")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackInteractive extends Model<FeedbackInteractive> implements Serializable {
    private static final long serialVersionUID = 1032101247190089921L;

    /** ID（唯一主键）;唯一主键 */

    @ApiModelProperty(value = "互动消息ID", name = "id", dataType = "Long", required = true)
    @TableId(type= IdType.ID_WORKER)
    private Long id ;
    /** 用户ID;用户ID */
    @ApiModelProperty(value = "用户ID", name = "userId", dataType = "String", required = true)
    private String userId ;
    /** 用户名称;用户名称 */
    @ApiModelProperty(value = "用户名称", name = "userName", dataType = "Long", required = true)
    private String userName ;
    /** 处理人  */
    @ApiModelProperty(value = "处理人", name = "handler", dataType = "String", required = true)
    private String handler ;
    /** 问题ID;问题ID */
    @ApiModelProperty(value = "反馈消息ID", name = "id", dataType = "Long", required = true)
    private Long feedbackId ;
    /** 问题类型名称;问题类型名称 */
    @ApiModelProperty(value = "问题类型名称", name = "name", dataType = "String", required = true)
    private String name ;
    /** 互动内容;互动内容 */
    @ApiModelProperty(value = "互动内容", name = "content", dataType = "String", required = true)
    private String content ;
    /** 创建时间;创建时间 */
    @ApiModelProperty(value = "创建时间", name = "createTime", dataType = "Date", required = true)
    private Date createTime ;
    /** 更新时间;更新时间 */
    @ApiModelProperty(value = "更新时间", name = "updateTime", dataType = "Date", required = true)
    private Date updateTime ;
}
