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
    @ApiModelProperty(value = "银行卡号", name = "bankNo", dataType = "Long", required = true)
    private String bankNo;
    //持卡人姓名 持卡人姓名
    @ApiModelProperty(value = "持卡人姓名", name = "cardholderName", dataType = "String", required = true)
    private String cardholderName;
    //银行名称 银行名称
    @ApiModelProperty(value = "银行名称", name = "bankName", dataType = "String", required = true)
    private String bankName;
    //支行名称
    @ApiModelProperty(value = "支行名称", name = "bankName", dataType = "String", required = true)
    private String bankBranch ;
    //IFSC代码;IFSC代码
    @ApiModelProperty(value = "IFSC代码", name = "ifsc", dataType = "String", required = true)
    private String ifsc;
    // 详细地址;详细地址
    @ApiModelProperty(value = "详细地址", name = "address", dataType = "String", required = true)
    private String address;
    //电话;电话号码
    @ApiModelProperty(value = "电话号码", name = "phone", dataType = "String", required = true)
    private String phone;
}
