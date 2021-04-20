package com.hy.account.stream;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2021/2/3 1:22
 * @UpdateUser: 寒夜
 * @UpdateDate: 2021/2/3 1:22
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SynchronousOrder implements Serializable {

    private static final long serialVersionUID = -2593169157948060728L;
    private Long orderId;
    private String userId;
    private Double walletAccount;
    private Double dailyInterestAmount;
}

