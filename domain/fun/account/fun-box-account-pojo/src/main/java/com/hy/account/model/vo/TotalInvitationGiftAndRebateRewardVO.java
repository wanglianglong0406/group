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
@ApiModel(value = "邀请奖励和返利奖励对象VO", description = "返回参数VO")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TotalInvitationGiftAndRebateRewardVO implements Serializable {
    private static final long serialVersionUID = 5376433866295211830L;
    @ApiModelProperty(value = "邀请奖励", name = "totalInvitationGift", dataType = "Double", required = true)
    private Double totalInvitationGift;
    @ApiModelProperty(value = "返利奖励", name = "totalRebateReward", dataType = "Double", required = true)
    private Double totalRebateReward;
}
