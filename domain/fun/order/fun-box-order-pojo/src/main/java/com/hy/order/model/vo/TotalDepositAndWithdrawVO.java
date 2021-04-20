package com.hy.order.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/12/28 13:00
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/12/28 13:00
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "存款总金额和取款总金额对象VO", description = "返回参数VO")
public class TotalDepositAndWithdrawVO {

    //存款
    @ApiModelProperty(value = "存款总额", name = "totalDeposit", dataType = "Double", required = true)
    private Double totalDeposit;
    //取款
    @ApiModelProperty(value = "提现总额", name = "totalWithdraw", dataType = "Double", required = true)
    private Double totalWithdraw;
}
