package com.hy.account.model;

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
 * @CreateDate: 2020/12/30 14:19
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/12/30 14:19
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BankcardInfo extends Model<BankcardInfo> implements Serializable {
    private static final long serialVersionUID = -5445905532095440039L;

    /** 银行卡ID;银行卡ID唯一主键 */
    @TableId(type = IdType.UUID)
    private String bankId ;
    /** 用户ID;用户ID */
    private String userId ;
    /** 账户ID;账户ID */
    private Long accountId ;
    /** 银行卡号;银行卡号 */
    private String bankNo ;
    /** 持卡人姓名;持卡人姓名 */
    private String cardholderName ;
    /** 银行名称;银行名称 */
    private String bankName ;
    /** 支行名称;支行名称 */
    private String bankBranch ;
    /** IFSC代码;IFSC代码 */
    private String ifsc ;
    /** 银行类型;银行类型 */
    private Integer bankTypeId ;
    /** 详细地址;详细地址 */
    private String address ;
    /** 银行类型名称;银行类型名称 */
    private String bankTypeName ;
    /** 电话;电话号码 */
    private String phone ;
    /** 是否默认银行卡(Y ：默认： N：其他) */
    private Integer bankIsDefault ;
    /** 创建时间;创建时间 */
    @JsonIgnore
    private Date createTime ;
    /** 更新时间;更新时间 */
    @JsonIgnore
    private Date updateTime ;
}
