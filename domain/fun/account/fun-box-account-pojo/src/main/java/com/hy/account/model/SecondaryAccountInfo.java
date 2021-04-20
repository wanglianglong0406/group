package com.hy.account.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/12/20 17:55
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/12/20 17:55
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SecondaryAccountInfo {
    //账户类型
    private Integer accountType;
    //账户类型名称
    private String accountTypeName;

    //账户余额 账户余额
    private Double accountBlance;
}
