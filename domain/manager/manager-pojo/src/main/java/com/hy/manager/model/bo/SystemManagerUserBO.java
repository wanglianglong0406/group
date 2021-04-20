package com.hy.manager.model.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2021/1/30 13:18
 * @UpdateUser: 寒夜
 * @UpdateDate: 2021/1/30 13:18
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@ApiModel(value = "系统管理平台用户对象BO", description = "从客户端，由用户传入的数据封装在此entity中")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SystemManagerUserBO implements Serializable {

    private static final long serialVersionUID = 2115829275703614289L;
    /**
     * 用户名;用户名
     */
    @ApiModelProperty(value = "用户名", name = "userName", dataType = "String", required = true)
    private String userName;
    /**
     * 用户登录密码;用户登录密码
     */
    @ApiModelProperty(value = "用户登录密码", name = "password", dataType = "String", required = true)
    private String password;
    /**
     * 确认密码
     */
    @ApiModelProperty(value = "确认密码", name = "confirmPassword", dataType = "String", required = true)
    private String confirmPassword;
    /**
     * 平台渠道;平台渠道（1：博彩平台  2：幸运商店）
     */
    @ApiModelProperty(value = "平台渠道（1：博彩平台  2：幸运商店）", name = "platformChannel", dataType = "Integer", required = true)
    private Integer platformChannel;
}
