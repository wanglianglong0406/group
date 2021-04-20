package com.hy.center.model.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2021/2/8 7:09
 * @UpdateUser: 寒夜
 * @UpdateDate: 2021/2/8 7:09
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnnouncementBO implements Serializable {

    /**
     * 公告id;公告id
     */
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

}
