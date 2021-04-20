package com.hy.account.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;


/**
 * @Description: $- 账户信息表 账户信息表(Account)表实体类 -$ #-->
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
public class Account extends Model<Account> implements Serializable {
    private static final long serialVersionUID = -6080755098183516061L;
    //账户ID 账户id唯一主键
    @TableId(type = IdType.ID_WORKER)
    private Long accountId;
    //用户ID 用户id
    private String userId;
    //账户号 账户号
    private Long accountNo;
    //账户等级 账户等级（0：一级账户 1：二级账户）
    private Integer accountLeve;
    //账户类型
    private Integer accountType;
    //账户类型名称
    private String accountTypeName;
    //账户余额 账户余额
    private Double accountBlance;
    //账户状态 账户状态（0：正常 1：冻结  2：注销 3开户中）
    private Integer accountStatus;
    //账户密码 账户密码
    private String accountPassword;
    /**
     * 签到奖励金额;签到奖励金额
     */
    private Double rewardAmount;
    /**
     * 升级奖励;升级奖励
     */
    private Double upgradeRewardBlance;
    /**
     * 月票红包;月票红包
     */
    private Double monthlyTicketBlance;
    //充值返利金额
    private Double rechargeRebateBlance;
    //佣金
    private Double commissionBlance;
    //邀请奖励
    private Double invitationRewardBlance;




    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;

}