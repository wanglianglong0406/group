package com.hy.lottery.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * (ParityRecord)表实体类
 *
 * @author makejava
 * @since 2020-11-23 15:35:57
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BetRecord extends Model<BetRecord> {
    private static final long serialVersionUID = -2339043346865061308L;
    /**
     * ID（唯一主键）;唯一主键
     */
    @TableId(type = IdType.ID_WORKER)
    private Long id;
    /**
     * 期彩代号
     */
    private String period;
    /**
     * 选择球;加入球的颜色
     * 1：红球
     * 2：绿球
     * 3：紫罗兰
     */
    private String selectColour;
    /**
     * 结果
     */
    private String result;
    /**
     * 奇偶数字;选择的奇偶数字
     */
    private String number;
    /**
     * 系统订单号;系统订单号
     */
    private Long orderId;
    /**
     * 用户Id;用户ID
     */
    private String userId;
    /**
     * 类型代码;类型代码
     */
    private String type;
    /**
     * 创建时间;创建时间
     */
    private Date createTime;
    /**
     * 更新时间;更新时间
     */
    private Date updateTime;
    /**
     * 投注金额;投注金额
     */
    private Double betAmount;
    /**
     * 开奖状态;开奖状态 1：未开 2：已开
     */
    private Integer openStatus;
    /**
     * 是否中奖;是否幸运中奖 1：未中  2：中奖了
     */
    private Integer isLuckFlag;
    /**
     * 支付状态 1：未支付  2：已支付
     */
    private Integer payStatus;
    /**
     * 赔率;赔率
     */
    private Double odds;
    /**
     * 输赢;输赢
     */
    private Double winOrLose;
    /**
     * 价格
     */
    private Long price;


}