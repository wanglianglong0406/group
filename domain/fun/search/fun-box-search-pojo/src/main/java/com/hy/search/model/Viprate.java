package com.hy.search.model;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: $- 贵宾特权 -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/12/13 18:20
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/12/13 18:20
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Viprate extends Model<Viprate> implements Serializable {

    private static final long serialVersionUID = -8188583838700807328L;
    /** ID（唯一主键）;唯一主键 */
    @JsonIgnore
    private Long id ;
    /** 名称;名称 */
    private String name ;
    /** VIP等级;VIP等级 */
    private Integer vipLevel ;
    /** 体育返利;体育返利 */
    private Double sportRebate ;
    /** 实时游戏返利;实时游戏返利 */
    private Double liveGameRebate ;
    /** 国际象棋游戏返利;国际象棋游戏返利 */
    private Double chessGameRebate ;
    /** 电子游戏返利;电子游戏返利 */
    private Double eGamingRebate ;
    /** 钓鱼游戏返利;钓鱼游戏返利 */
    private Double fishingGameRebate ;
    /** 每日取款（次）;每日取款（次） */
    private Integer dailyWithdrawal ;
    /** 每日取款限额（万）;每日取款限额（万） */
    private Integer dailyWithdrawalLimit ;
    /** 创建时间;创建时间 */
    @JsonIgnore
    private Date createTime ;
    /** 更新时间;更新时间 */
    @JsonIgnore
    private Date updateTime ;
}
