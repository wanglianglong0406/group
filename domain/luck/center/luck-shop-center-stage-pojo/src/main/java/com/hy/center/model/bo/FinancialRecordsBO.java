package com.hy.center.model.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/12/30 18:37
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/12/30 18:37
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FinancialRecordsBO implements Serializable {
    private static final long serialVersionUID = 457935328992322767L;


    /**
     * 用户ID;用户ID
     */
    private String userId;
    /**
     * 用户昵称;用户昵称
     */
    private String nicknames;
    /**
     * 系统订单号;系统订单号
     */
    private Long orderId;
    /**
     * 产品Id;产品Id
     */
    private Integer productId;
    /**
     * 购买金额;购买金额
     */
    private Double amount;


}
