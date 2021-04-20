package com.hy.user.model;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.*;

import java.io.Serializable;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2021/1/4 17:46
 * @UpdateUser: 寒夜
 * @UpdateDate: 2021/1/4 17:46
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAgentDistributionInfo extends Model<UserAgentDistributionInfo> implements Serializable {
    private static final long serialVersionUID = 8168408111879849455L;

    /**
     * ID（唯一主键）;唯一主键
     */
    private Integer id;
    /**
     * 邀请数量;邀请数量
     */
    private Integer size;
    /**
     * 邀请奖励;邀请奖励
     */
    private Double inviteRewards;
    /**
     * 分销利率;分销利率
     */
    private Double distributionRate;
    /**
     * 会员等级;会员等级
     */
    private Integer vipLevel;
    /**
     * 代理等级;代理等级
     */
    private Integer agencyLevel;
    /**
     * 日息佣金利率;日息佣金利率利率
     */
    private Double dailyInterestCommissionRate;
    /**
     * 充值回扣利率;充值回扣利率
     */
    private Double rechargeRebateRate;

}
