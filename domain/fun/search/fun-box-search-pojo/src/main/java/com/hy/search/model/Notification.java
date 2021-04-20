package com.hy.search.model;

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
 * @Description: $- 通知 -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/12/17 15:14
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/12/17 15:14
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Notification extends Model<Notification> implements Serializable {
    private static final long serialVersionUID = 6577631247647009288L;

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
     * 类型;类型（1 匹配  2 活动 3 系统通知 4 我的通知）
     */
    private Integer type;
    /**
     * 标题;标题
     */
    private String title;
    /**
     * 内容;内容
     */
    private String content;
    /**
     * 是否已读;是否已读(1 未读  2 已读 )
     */
    private Integer isRead;
    /**
     * 是否删除;是否删除（1 未删除  2 已删除）
     */
    private Integer isDelete;
    /**
     * 创建时间;创建时间
     */
    private Date createTime;
    /**
     * 更新时间;更新时间
     */
    private Date updateTime;
}
