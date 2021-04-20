package com.hy.user.model;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: $- 反馈类型信息表-$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/12/18 21:12
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/12/18 21:12
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@ApiModel(value = "反馈类型信息", description = "反馈类型信息")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackType extends Model<FeedbackType> implements Serializable {

    private static final long serialVersionUID = 707838061782907429L;
    /**
     * ID（唯一主键）;唯一主键
     */
    @ApiModelProperty(value = "反馈类型ID", name = "id", dataType = "Long", required = true)
    private Long id;
    /**
     * 问题类型名称;问题类型名称
     */
    @ApiModelProperty(value = "反馈类型名称", name = "name", dataType = "String", required = true)
    private String name;
    /**
     * 创建时间;创建时间
     */
    @ApiModelProperty(value = "创建时间", name = "createTime", dataType = "Date", required = true)
    @JsonIgnore
    private Date createTime;
    /**
     * 更新时间;更新时间
     */
    @ApiModelProperty(value = "更新时间", name = "updateTime", dataType = "Date", required = true)
    @JsonIgnore
    private Date updateTime;
}
