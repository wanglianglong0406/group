package com.hy.account.model;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: $- 账户类型 -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/12/20 15:58
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/12/20 15:58
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountType extends Model<AccountType> {
    private static final long serialVersionUID = 4854909112886218976L;

    /**
     * ID（唯一主键）;唯一主键
     */
    private Integer id;
    /**
     * name;账户类型名称
     */
    private String name;

}
