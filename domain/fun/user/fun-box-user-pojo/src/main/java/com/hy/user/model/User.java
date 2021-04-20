package com.hy.user.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


/**
 * @Description: $- 用户信息表 用户信息表(User)表实体类 -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/11/19 15:50
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/11/19 15:50
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends Model<User> {
    private static final long serialVersionUID = 3297483845136317778L;
    //用户ID 用户id
    @TableId(type= IdType.UUID)
    private String userId;
    //用户名 用户名
    private String userName;
    //用户头像 用户头像
    private String userFace;
    //用户手机号 用户手机号
    private String userMobilePhone;
    //用户注册码 用户注册码
    private String userRegistrationCode;
    //用户邀请码 用户邀请码
    private String userInvitationCode;
    //用户登录密码 用户登录密码
    private String userLoginPassword;
    //用户性别 用户性别（0：男 1：女 2：保密）
    private Integer userSex;
    //用户会员等级 用户会员等级
    private Integer userMembershipLevel;
    //用户生日 用户生日
    private Object userBirthday;
    //用户状态 用户状态（0：正常  1：注销 2：在线 3：离线）
    private Integer userStatus;
    /** 用户等级;用户等级
     0 ：系统管理员用户
     1 ：系统用户
     2 ：普通用户 */
    private Integer userLevel ;
    /** 用户来源;用户来源
     1：H5
     2：APP
     3：PC
     4：其他 */
    private String source ;
    //创建时间 创建时间
    private Date createTime;
    //更新时间 更新时间
    private Date updateTime;





}