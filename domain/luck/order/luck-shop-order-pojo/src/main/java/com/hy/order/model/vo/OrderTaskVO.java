package com.hy.order.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2021/1/9 14:38
 * @UpdateUser: 寒夜
 * @UpdateDate: 2021/1/9 14:38
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderTaskVO implements Serializable {


    private static final long serialVersionUID = -8791555161670677759L;
    private String userId;
    private String orderId;
    private Long itemsId;
    private String itemName;
    private String url;
    private String content;
    private Double price;
    private Double commissionRate;
    private Double commissionRewarsAmount;
    private Integer numberOfTasks;
    private Integer numberOfRemainingTasks;
    private Integer numberOfTasksCompleted;

}
