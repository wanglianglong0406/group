package com.hy.user.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/12/29 12:43
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/12/29 12:43
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends Model<User> implements Serializable {

    private static final long serialVersionUID = 7552107554635214414L;

    /**
     * 用户ID;用户id
     */
    @TableId(type = IdType.UUID)
    private String userId;
    /**
     * 用户名;用户名
     */
    private String userName;
    /**
     * 昵称;昵称
     */
    private String nicknames;
    /**
     * 用户手机号;用户手机号
     */
    private String mobilePhone;
    /**
     * 用户注册码;用户注册码
     */
    private String registrationCode;
    /**
     * 用户邀请码;用户邀请码
     */
    private String invitationCode;
    /**
     * 用户登录密码;用户登录密码
     */
    @JsonIgnore
    private String password;
    /**
     * 用户性别;用户性别（0：男 1：女 2：保密）
     */
    private Integer sex;
    /**
     * 用户会员等级;用户会员等级
     */
    private Integer membershipLevel;
    /**
     * 用户生日;用户生日
     */
    private Date birthday;
    /**
     * 用户状态;用户状态（0：正常  1：注销  2：在线  3 ：离线）
     */
    private Integer userStatus;
    /**
     * 用户等级;用户等级
     * 0 ：系统管理员用户
     * 1 ：系统用户
     * 2 ：普通用户
     */
    private Integer userLevel;
    /**
     * 用户来源;用户来源 1：H5 2：APP 3：PC  4：其他
     */
    private String source;
    /**
     * 团队大小;团队大小
     */
    private Integer teamSize;
    /**
     * 代理等级;代理等级 （0 Non agency  1 LEVEL1  2 LEVEL2   3 LEVEL3）
     */
    private Integer agencyLevel;
    /**
     * 更新时间;更新时间
     */
    private LocalDate updateTime;
    /**
     * 注册时间;注册时间
     */
    private LocalDate createTime;
}
