package com.hy.user.model.vo;

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
 * @CreateDate: 2020/12/28 12:12
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/12/28 12:12
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@ApiModel(value = "邀请记录对象VO", description = "返回参数VO")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvitationRecordVO implements Serializable {
    private static final long serialVersionUID = -3741717692420341768L;

    //用户ID
    @ApiModelProperty(value = "用户ID", name = "userId", dataType = "String",required = true)
    private String userId;
    //用户名称
    @ApiModelProperty(value = "用户名称", name = "userName", dataType = "String",required = true)
    private String userName;
    //用户状态 用户状态（0：正常  1：注销 2：在线 3：离线）
    @ApiModelProperty(value = "用户状态（0：正常  1：注销 2：在线 3：离线）", name = "userStatus", dataType = "Integer",required = true)
    private Integer userStatus;
    //奖励类别
    @ApiModelProperty(value = "奖励类别", name = "rewardCategory", dataType = "String",required = true)
    private String rewardCategory;
    //奖励金额
    @ApiModelProperty(value = "奖励金额", name = "rewardAmount", dataType = "Double",required = true)
    private Double rewardAmount;
    //创建时间 创建时间
    @ApiModelProperty(value = "时间", name = "createTime", dataType = "obj",required = true)
    private Date createTime;

}
