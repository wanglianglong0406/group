package com.hy.center.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2021/2/8 7:09
 * @UpdateUser: 寒夜
 * @UpdateDate: 2021/2/8 7:09
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Announcement extends Model<Items> implements Serializable {

    private static final long serialVersionUID = -5770821985417735477L;
    /**
     * 公告id;公告id
     */
    @TableId(type = IdType.ID_WORKER_STR)
    private String id;
    /**
     * 公告标题;公告标题
     */
    private String title;
    /**
     * 公告内容;公告内容
     */
    private String content;
    /**
     * 公告类型;公告类型1：普通公告 2：其他公告  3:系统公告
     */
    private Integer type;
    /**
     * 公告状态;1:open 2 :close
     */
    private Integer status;
    /**
     * 是否弹窗;1：yes  2: no
     */
    private Integer popUp;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
}
