package com.hy.user.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Description: $- (SignHistory)表实体类 -$ #-->
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
public class SignInfoHistroy extends Model<SignInfoHistroy> {
    //签到历史记录ID
    @TableId(type= IdType.ID_WORKER)
    //ID（唯一主键） 唯一主键
    private Long id;
    //创建时间 创建时间
    private Date createTime;
    //更新时间 更新时间
    private Date updateTime;
    //签到日程唯一ID
    private Long signId;
    //签到状态 签到状态（1：已签到 2：未签到）
    private Integer signStatus;
    //用户ID 用户ID
    private String userId;
    //签到统计 签到统计
    private Integer countTotal;
    //是否重置签到信息 是否重置签到信息 1 已重置  2 未重置
    private Integer isResetFlag;
    //最后签到日期 最后签到日期
    private Date lastSignTime;
    //连续签到累积次数 连续签到累积次数
    private Integer continuousSignInCount;
    //签到星期 签到星期
    private String weekDay;



}