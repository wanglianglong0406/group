package com.hy.manager.model.bo;

import io.swagger.annotations.ApiModelProperty;
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

    private static final long serialVersionUID = 2019249215254974025L;
    /**
     * 公告id;公告id
     */
    @ApiModelProperty(value = "唯一主键（商品ID）(编辑时必填，添加非必填)", name = "id", dataType = "Long")
    private String id;
    /**
     * 公告标题;公告标题
     */
    @ApiModelProperty(value = "公告标题", name = "title", dataType = "String")
    private String title;
    /**
     * 公告内容;公告内容
     */
    @ApiModelProperty(value = "公告内容", name = "content", dataType = "String")
    private String content;
    /**
     * 公告类型;公告类型1：普通公告 2：其他公告  3:系统公告
     */
    @ApiModelProperty(value = "公告类型1：普通公告 2：其他公告  3:系统公告", name = "type", dataType = "Integer")
    private Integer type;
    /**
     * 公告状态;1:open 2 :close
     */
    @ApiModelProperty(value = " 公告状态;1:open 2 :close", name = "id", dataType = "Integer")
    private Integer status;
    /**
     * 是否弹窗;1：yes  2: no
     */
    @ApiModelProperty(value = "是否弹窗;1：yes  2: no", name = "popUp", dataType = "Integer")
    private Integer popUp;

}
