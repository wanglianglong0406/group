package com.hy.account.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/12/29 17:04
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/12/29 17:04
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account extends Model<Account> implements Serializable {
    private static final long serialVersionUID = -2092656557337296676L;

    /**
     * 账户ID;账户id唯一主键
     */
    @TableId(type = IdType.ID_WORKER)
    private Long accountId;
    /**
     * 用户ID;用户id
     */
    private String userId;
    /**
     * 账户号;账户号
     */
    private Long accountNo;
    /**
     * 账户等级;账户等级（0：一级账户 1：二级账户）
     */
    private Integer accountLeve;
    /**
     * 钱包账户;钱包账户
     */
    private Double walletAccount;
    /**
     * 账户状态;账户状态（0：正常 1：冻结  2：注销）
     */
    private Integer accountStatus;
    /**
     * 账户密码;账户密码
     */
    private String accountPassword;
    /**
     * 虚拟账户;虚拟账户
     */
    private Double virtualAccount;
    /**
     * 资金冻结账户;资金冻结账户
     */
    private Double frozenAccounts;
    /**
     * 金融账户;金融账户
     */
    private Double financialAccount;
    /**
     * 当天收入账户;当天收入账户（凌晨清零）
     */
    private Double dayIncomeAccount;
    /**
     * 个人收入账户;个人收入账户
     */
    private Double personalIncomeAccount;
    /**
     * 团队贡献账户;团队贡献账户
     */
    private Double teamContributionAccount;
    /**
     * 创建时间;创建时间
     */
    private Date createTime;
    /**
     * 更新时间;更新时间
     */
    private Date updateTime;

}
