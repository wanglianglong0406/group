package com.hy.search.model;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: $- 平台信息 -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/12/22 23:16
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/12/22 23:16
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlatfromInfo extends Model<PlatfromInfo> implements Serializable {
    private static final long serialVersionUID = 7023986578595059830L;

    /**
     * ID（唯一主键）;平台ID
     */
    private Integer id;
    /**
     * 平台名称;平台名称
     */
    private String name;
    /**
     * 是否启用;是否启用 1 启用  2 禁用
     */
    private Integer enableIsFlag;
}
