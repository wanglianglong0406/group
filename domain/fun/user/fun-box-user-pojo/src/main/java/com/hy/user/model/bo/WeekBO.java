package com.hy.user.model.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/11/20 21:06
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/11/20 21:06
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeekBO {

    /** 星期一;星期一 */
    private String monday ;
    /** 星期二;星期二 */
    private String tuesday ;
    /** 星期三;星期三 */
    private String wednesday ;
    /** 星期四;星期四 */
    private String thursday ;
    /** 星期五;星期五 */
    private String friday ;
    /** 星期六;星期六 */
    private String sunday ;
    /** 星期天;星期天 */
    private String saturday ;
}
