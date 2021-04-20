package com.hy.center.model;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: $- 金融产品信息 -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/12/30 15:02
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/12/30 15:02
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FinancialProducts extends Model<FinancialProducts> implements Serializable {
    private static final long serialVersionUID = -6028586174084683217L;
    /** ID（唯一主键）;唯一主键 */
    private Integer id ;
    /** 周期;周期周期 */
    private Integer cycle ;
    /** 利息;利息 */
    private Double interest ;
    /** 总收入;总收入 */
    private Double totalRevenue ;
    /** 创建时间;创建时间 */
    @JsonIgnore
    private Date createTime ;
    /** 更新时间;更新时间 */
    @JsonIgnore
    private Date updateTime ;
}
