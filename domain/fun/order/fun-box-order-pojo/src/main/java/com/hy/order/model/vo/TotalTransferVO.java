package com.hy.order.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/12/24 18:34
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/12/24 18:34
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TotalTransferVO implements Serializable {
    private static final long serialVersionUID = -5940504408566093932L;

    //转入金额
    private Double totalTransferInAmount;
    //转出金额
    private Double totalTransferOutAmount;
}
