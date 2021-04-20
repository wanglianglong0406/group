package com.hy.search.model;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 公告信息表(Announcement)表实体类
 *
 * @author 寒夜
 * @since 2020-11-18 17:47:27
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Announcement extends Model<Announcement> implements Serializable {
    //公告id
    private Integer id;
    //公告标题
    private String title;
    //公告内容
    private String content;
    //公告类型 公告类型1：普通公告 2：其他公告  3:系统公告
    private Integer type;
    //公告状态 1:open 2 :close
    private Integer status;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;

}