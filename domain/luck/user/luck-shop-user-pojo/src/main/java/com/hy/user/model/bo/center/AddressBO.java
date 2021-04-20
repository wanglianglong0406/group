package com.hy.user.model.bo.center;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/12/29 13:16
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/12/29 13:16
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@ApiModel(value = "地址对象BO", description = "从客户端，由用户传入的数据封装在此entity中")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressBO implements Serializable {

    private static final long serialVersionUID = 1467341596137497202L;
    /**
     * 省份;省份
     */
    @ApiModelProperty(value = "省份", name = "province", dataType = "String", required = true)
    private String province;
    /**
     * 城市;城市
     */
    @ApiModelProperty(value = "城市", name = "city", dataType = "String", required = true)
    private String city;
    /**
     * 区域;地区
     */
    @ApiModelProperty(value = "区域", name = "district", dataType = "String", required = true)
    private String district;
    /**
     * 街道;街道
     */
    @ApiModelProperty(value = "街道", name = "streets", dataType = "String", required = true)
    private String streets;
    /**
     * 详细地址;详细地址
     */
    @ApiModelProperty(value = "详细地址", name = "detailedAddress", dataType = "String", required = true)
    private String detailedAddress;
    /**
     * 姓名;真实姓名
     */
    @ApiModelProperty(value = "真实姓名", name = "realName", dataType = "String", required = true)
    private String realName;
    /**
     * 移动电话;移动电话
     */
    @ApiModelProperty(value = "移动电话", name = "mobilePhone", dataType = "String", required = true)
    private String mobilePhone;
    /**
     * 状态;状态
     */
    @ApiModelProperty(value = "状态", name = "state", dataType = "String", required = true)
    private String state;
    /**
     * 用户ID;用户ID
     */
    @ApiModelProperty(value = "用户ID", name = "userId", dataType = "String", required = true)
    private String userId;

}
