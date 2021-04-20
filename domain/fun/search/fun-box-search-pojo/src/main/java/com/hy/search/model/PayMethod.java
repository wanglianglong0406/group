package com.hy.search.model;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/12/24 13:05
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/12/24 13:05
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PayMethod extends Model<PayMethod> implements Serializable {
    private static final long serialVersionUID = -5639607488500541882L;

    /** ID（唯一主键）;唯一主键（支付方式ID） */
    private Integer id ;
    /** 支付方式名称;支付方式名称 */
    private String payMethodName ;
    /** 支付标题;支付标题 */
    private String title ;
    /** 是否启用;是否启用 1 启用  2 禁用 */
    private Integer enableIsFlag ;
}
