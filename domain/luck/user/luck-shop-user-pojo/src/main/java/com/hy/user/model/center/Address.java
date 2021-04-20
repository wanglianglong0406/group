package com.hy.user.model.center;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/12/29 13:14
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/12/29 13:14
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Address extends Model<Address> implements Serializable {
    private static final long serialVersionUID = 830651193845299533L;

    /** ID（唯一主键）;唯一主键 */
    @TableId(type= IdType.ID_WORKER)
    private Long id ;
    /** 省份;省份 */
    private String province ;
    /** 城市;城市 */
    private String city ;
    /** 区域;地区 */
    private String district ;
    /** 街道;街道 */
    private String streets ;
    /** 详细地址;详细地址 */
    private String detailedAddress ;
    /** 姓名;真实姓名 */
    private String realName ;
    /** 移动电话;移动电话 */
    private String mobilePhone ;
    /** 状态;状态 */
    private String state ;
    /** 用户ID;用户ID */
    private String userId ;
    /** 创建时间;创建时间 */
    @JsonIgnore
    private Date createTime ;
    /** 更新时间;更新时间 */
    @JsonIgnore
    private Date updateTime ;
}
