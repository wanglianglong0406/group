package com.hy.user.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: $- MybatisPlusConfig  分页插件 -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/12/2 9:21
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/12/2 9:21
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Configuration
public class MybatisPlusConfig {
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor page = new PaginationInterceptor();
        page.setDialectType("mysql");
        return page;
    }

}
