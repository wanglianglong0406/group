package com.hy.center.model;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.*;

import java.io.Serializable;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/12/30 15:05
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/12/30 15:05
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PriceInfo extends Model<FinancialProducts> implements Serializable {
    private static final long serialVersionUID = 8630186943356166456L;

    /** ID（唯一主键）;唯一主键 */
    private Integer id ;
    /** 价格*/
    private Double price  ;
}
