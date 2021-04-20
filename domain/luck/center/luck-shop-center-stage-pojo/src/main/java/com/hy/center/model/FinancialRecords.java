package com.hy.center.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/12/30 18:37
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/12/30 18:37
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FinancialRecords extends Model<FinancialRecords> implements Serializable {
    private static final long serialVersionUID = 457935328992322767L;

    /**
     * ID（唯一主键）;唯一主键
     */
    @TableId(type = IdType.ID_WORKER)
    private Long id;
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
     * 状态;状态 1 生效中 2 已生效 3 已失效
     */
    private Integer status;
    /**
     * 产品Id;产品Id
     */
    private Integer productId;
    /**
     * 周期;周期
     */
    private Integer cycle;
    /**
     * 天数;天数
     */
    private Integer days;
    /**
     * 利息;利息
     */
    private Double interest;
    /**
     * 总利息;总利息
     */
    private Double totalRevenue;
    /**
     * 购买金额;购买金额
     */
    private Double amount;
    /**
     * 预期收益;预期收益
     */
    private Double expectedEarning;
    /**
     * 实际收益;实际收益
     */
    private Double actualEarnings;
    /**
     * 日息
     */
    private Double dailyInterest;
    /**
     * 购买时间;创建时间
     */
    private Date createTime;
    /**
     * 更新时间;更新时间
     */
    private Date updateTime;
    /**
     * 开始日期;开始日期
     */
    private LocalDate startDate;
    /**
     * 结束日期;结束日期
     */
    private LocalDate endDate;
    /**
     * 最后结算时间;最后结算时间
     */
    private Date finalSettlementTime;
}
