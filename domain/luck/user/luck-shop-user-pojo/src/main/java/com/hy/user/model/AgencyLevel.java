package com.hy.user.model;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.*;

import java.io.Serializable;

/**
 * @Description: $- 代理等级 -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2021/1/3 16:28
 * @UpdateUser: 寒夜
 * @UpdateDate: 2021/1/3 16:28
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AgencyLevel extends Model<User> implements Serializable {

    private static final long serialVersionUID = -3890903278494738219L;

    /**
     * ID（唯一主键）;唯一主键
     */
    private Integer id;
    /**
     * 最小成团人数;最小成团人数
     */
    private Integer minSize;
    /**
     * 最大成天人数;最大成天人数
     */
    private Integer maxSize;
    /**
     * 代理等级;代理等级 （0 Non agency  1 LEVEL1  2 LEVEL2   3 LEVEL3）
     */
    private Integer agencyLevel;
    /**
     * 代理等级名称;代理等级名称
     */
    private String agencyLevelName;
}
