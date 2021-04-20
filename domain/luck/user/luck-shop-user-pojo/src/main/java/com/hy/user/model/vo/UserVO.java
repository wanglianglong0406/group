package com.hy.user.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserVO implements Serializable {

    private static final long serialVersionUID = -6121086255080468403L;
    /**
     * 主键id 用户id
     */
    private String userId;

    /**
     * 用户名 用户名
     */
    private String userName;
    /**
     * 昵称 昵称
     */
    private String nickName;
    /**
     * 性别 性别 1:男  0:女  2:保密
     */
    private Integer sex;

    //用户会员等级 用户会员等级
    private Integer membershipLevel;

    //用户状态 用户状态（0：正常  1：注销 2：在线 3：离线）
    private Integer userStatus;

    //用户token
    private String userUniqueToken;


}