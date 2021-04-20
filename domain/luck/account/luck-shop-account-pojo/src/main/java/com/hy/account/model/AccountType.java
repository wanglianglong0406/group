package com.hy.account.model;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.*;

/**
 * @Description: $- 账户类型 -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/12/20 15:58
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/12/20 15:58
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountType extends Model<AccountType> {


    private static final long serialVersionUID = 7152140720591632040L;
    /**
     * ID（唯一主键）;唯一主键
     */
    private Integer id;
    /**
     * name;账户类型名称
     */
    private String name;

}
