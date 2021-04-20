package com.hy.user.model.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

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
    //用户名 用户名
    @ApiModelProperty(value = "用户名", name = "userName", dataType = "String", example = "hy", required = true)
    @NotBlank(message = "用户名不能为空!!!")
    private String userName;
    @ApiModelProperty(value = "确认密码", name = "confirmPassword", dataType = "String",  example = "123456", required = true)
    @NotBlank(message = "用户确认密码不能为空!!!")
    private String confirmPassword;
    //用户头像 用户头像
    @ApiModelProperty(value="用户头像", dataType = "String", name="userFace")
    private String userFace;
    //用户手机号 用户手机号
    @NotBlank(message = "用户手机号不能为空!!!")
    @ApiModelProperty(value="手机号", name="userMobilePhone",dataType = "String", example="1399999999", required = true)
    private String userMobilePhone;

    //用户邀请码 用户邀请码
    @NotBlank(message = "用户邀请码不能为空!!!")
    @ApiModelProperty(value="用户邀请码", name="userInvitationCode", dataType = "Integer", example="123456", required = true)
    private String userInvitationCode;
    //用户登录密码 用户登录密码
    @ApiModelProperty(value="用户登录密码", name="userLoginPassword", dataType = "String", example="123456", required = true)
    private String userLoginPassword;
    //用户性别 用户性别（0：男 1：女 2：保密）
    @Min(value = 0, message = "性别选择不正确")
    @Max(value = 2, message = "性别选择不正确")
    @ApiModelProperty(value="性别", name="userSex", dataType = "Integer",example="0:女 1:男 2:保密")
    private Integer userSex;
    //用户生日 用户生日
    @ApiModelProperty(value="生日", name="userBirthday", dataType = "Object", example="1900-01-01")
    private Object userBirthday;
    //手机验证码
    @ApiModelProperty(value="手机验证码", name="verificationCode",dataType = "String",  example="111111", required = true)
    private String verificationCode;


    /** 用户来源;用户来源
     1：H5  2：APP  3：PC  4：其他
     */
    @NotBlank(message = "Registration source cannot be empty ...")
    @ApiModelProperty(value="注册来源", name="verificationCode",dataType = "String",  example="H5 、 APP 、 PC  、其他（这技能填写其中的一个） ", required = true)
    private String source ;
}
