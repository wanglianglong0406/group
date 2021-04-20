package com.hy.account.model.vo;

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
 * @CreateDate: 2020/12/28 12:40
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/12/28 12:40
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@ApiModel(value = "邀请奖励和代理佣金奖励对象VO", description = "返回参数VO")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TotalInvitationRewardAndCommissionRewardVO implements Serializable {
    private static final long serialVersionUID = 5376433866295211830L;

    //邀请奖励
    @ApiModelProperty(value = "邀请奖励", name = "totalInvitationReward", dataType = "Double", required = true)
    private Double totalInvitationReward;
    //佣金奖励
    @ApiModelProperty(value = "代理佣金奖励", name = "totalCommissionReward", dataType = "Double", required = true)
    private Double totalCommissionReward;
}
