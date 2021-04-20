package com.hy.user.model.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @Description: $- UserBO -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/11/17 14:17
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/11/17 14:17
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@ApiModel(value = "用户对象BO", description = "从客户端，由用户传入的数据封装在此entity中")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserBO {
    //用户手机号 用户手机号
    @ApiModelProperty(value = "手机号", name = "mobilePhone", dataType = "String", example = "1399999999", required = true)
    private String mobilePhone;
    //用户邀请码 用户邀请码
    @ApiModelProperty(value = "用户邀请码", name = "invitationCode", dataType = "String", required = true)
    private String invitationCode;
    //用户邀请码 用户邀请码
    @ApiModelProperty(value = "手机验证码", name = "verificationCode", dataType = "String")
    private String verificationCode;
    //用户登录密码 用户登录密码
    @ApiModelProperty(value = "用户登录密码", name = "password", dataType = "String", required = true)
    private String password;
    @ApiModelProperty(value = "确认密码", name = "confirmPassword", dataType = "String", required = true)
    private String confirmPassword;
    //用户来源;用户来源 1：H5  2：APP  3：PC  4：其他
    @ApiModelProperty(value = "注册来源", name = "source", dataType = "String", example = "H5 、 APP 、 PC  、其他（这技能填写其中的一个） ", required = true)
    private String source;
}
