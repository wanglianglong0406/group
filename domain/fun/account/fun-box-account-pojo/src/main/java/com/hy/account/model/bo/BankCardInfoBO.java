package com.hy.account.model.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/11/20 14:08
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/11/20 14:08
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */

@ApiModel(value = "银行卡信息BO", description = "从客户端，由用户传入的数据封装在此entity中")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BankCardInfoBO {

    //用户ID 用户ID
    @ApiModelProperty(value = "用户ID", name = "userId", dataType = "String", required = true)
    private String userId;
    //银行卡号 银行卡号
    @ApiModelProperty(value = "用户名", name = "bankNo", dataType = "Long", required = true)
    private Long bankNo;
    //持卡人姓名 持卡人姓名
    @ApiModelProperty(value = "持卡人姓名", name = "cardholderName", dataType = "String", required = true)
    private String cardholderName;
    //银行名称 银行名称
    @ApiModelProperty(value = "银行名称", name = "bankName", dataType = "String", example = "XX商业银行", required = true)
    private String bankName;
    //银行类型 银行类型（1：商业银行 2： 民用银行）
    @ApiModelProperty(value = "银行类型", name = "bankType", dataType = "String", example = "（1：商业银行 2： 民用银行）", required = false)
    private String bankType;
    //是否默认银行卡 是否默认银行卡(0 ：默认： 1：其他)
    @ApiModelProperty(value = "是否默认银行卡", name = "bankIsDefault", dataType = "String", example = "(0 ：默认： 1：其他)", required = true)
    private String bankIsDefault;

    //IFSC代码;IFSC代码
    @ApiModelProperty(value = "IFSC代码", name = "ifsc", dataType = "String", required = true)
    private String ifsc;
}
