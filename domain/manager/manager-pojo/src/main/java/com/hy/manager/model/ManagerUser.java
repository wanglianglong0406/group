package com.hy.manager.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2021/1/30 13:08
 * @UpdateUser: 寒夜
 * @UpdateDate: 2021/1/30 13:08
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ManagerUser extends Model<ManagerUser> implements Serializable {
    private static final long serialVersionUID = 899085408290028196L;
    @TableId(type = IdType.UUID)
    private String userId;
    /**
     * 用户名;用户名
     */
    private String userName;
    /**
     * 用户登录密码;用户登录密码
     */
    @JsonIgnore
    private String password;
    /**
     * 用户状态;用户状态（0：正常  1：注销  2：在线  3 ：离线）
     */
    private Integer userStatus;
    /**
     * 用户等级;用户等级（0：超级管理员 1：普通系统用户）
     */
    private Integer userLevel;
    /**
     * 平台渠道;平台渠道（1：博彩平台  2：幸运商店）
     */
    private Integer platformChannel;
    /**
     * 更新时间;更新时间
     */
    private Date updateTime;
    /**
     * 注册时间;注册时间
     */
    private Date createTime;
}
