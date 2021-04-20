package com.hy.center.model;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.*;

import java.io.Serializable;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2021/1/4 17:44
 * @UpdateUser: 寒夜
 * @UpdateDate: 2021/1/4 17:44
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VipInfo extends Model<VipInfo> implements Serializable {
    private static final long serialVersionUID = -1569755627402867659L;

    /**
     * ID（唯一主键）;唯一主键
     */
    private Integer id;
    /**
     * VIP等级ID;VIP等级ID
     */
    private Integer vipLevel;
    /**
     * 名称;名称
     */
    private String name;
    /**
     * 资产要求;资产要求
     */
    private Double assets;
    /**
     * 订单任务佣金比率;订单任务佣金比率
     */
    private Double commissionRate;
    /**
     * 月取款限额;月取款限额
     */
    private Double monthlyWithdrawalLimit;
    /**
     * 日取款次数;日取款次数
     */
    private Integer numberOfDailyWithdrawals;
    /**
     * 任务数量;任务数量
     */
    private Integer numberOfTasks;
}
