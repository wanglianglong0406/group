package com.hy.user.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;


/**
 * @Description: $- (SignInfo)表实体类 -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/11/19 15:50
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/11/19 15:50
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignInfo extends Model<SignInfo> implements Serializable {
    private static final long serialVersionUID = -2692242644019941984L;
    @TableId(type= IdType.ID_WORKER)
    private Long id ;
    /** 签到星期;签到星期 */
    private String weekDay ;
    /** 累计签到统计总数;累计签到统计总数 */
    private Integer countTotal ;
    /** 创建时间;创建时间 */
    private Date createTime ;
    /** 更新时间;更新时间 */
    private Date updateTime ;
    /** 用户ID;用户ID */
    private String userId ;
    /** 连续签到累积次数;连续签到累积次数 */
    private Integer continuousSignInCount ;
    /** 最后签到日期;最后签到日期 */
    private Date lastSignTime ;
    /** 是否签到过;1：已签到    2： 未签到 */
    private Integer isFlag ;



}