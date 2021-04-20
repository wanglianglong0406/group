package com.hy.account.model.vo;

import lombok.*;

import java.io.Serializable;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/12/29 18:12
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/12/29 18:12
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountVO implements Serializable {

    private static final long serialVersionUID = 4700642241100814123L;
    /**
     * 钱包账户;钱包账户  可用资产
     */
    private Double availableAssets;
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
     * 团队规模大小
     */
    private Integer teamSize;


}
