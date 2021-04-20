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
 * @Description: $- 贵宾信息 -$ #-->
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
public class Vipinfo extends Model<Vipinfo> implements Serializable {
    private static final long serialVersionUID = -8700066639977648339L;

    /** ID（唯一主键）;唯一主键 */
    private Integer id ;
    /** 名称;名称 */
    private String name ;
    /** 升级奖励;升级奖励 */
    private Double upgradeReward ;
    /** 月票红包;月票红包 */
    private Double monthlyTicket ;
    /** 现金流;现金流 */
    private Double cashFlow ;
    /** 保留现金流;保留现金流 */
    private Double retainedCashFlow ;
    /** 生日礼物;生日礼物 */
    private String birthdayGift ;
    /** 小图标;小图标 */
    private String image ;
    /** 主图;创建主图 */
    private String mainImage ;
    /** 更新时间;更新时间 */
    @JsonIgnore
    private Date updateTime ;
    /** 创建时间;创建时间 */
    @JsonIgnore
    private Date createTime ;
}
