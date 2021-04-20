package com.hy.search.model;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: $- 价格信息 -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/12/17 15:16
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/12/17 15:16
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AmountInfo extends Model<AmountInfo> implements Serializable {
    private static final long serialVersionUID = 8353264519218956419L;

    /** ID（唯一主键）;唯一主键 */
    private Long id ;
    /** 金额;金额 */
    private Double amount ;
    /** 创建时间;创建时间 */
    @JsonIgnore
    private Date createTime ;
    /** 更新时间;更新时间 */
    @JsonIgnore
    private Date updateTime ;
}
