package com.hy.center.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/12/31 18:01
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/12/31 18:01
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FinancialEarningsVO implements Serializable {
    //已经获得利息
    private Double hasEarnedInterest;
    //购买金额
    private Double purchaseAmount;
    //预期收益
    private Double expectedEarningInterest;
    //总资产
    private Double totalAssets;

}
