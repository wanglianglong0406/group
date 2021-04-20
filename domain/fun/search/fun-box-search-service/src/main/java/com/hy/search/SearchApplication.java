package com.hy.search;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Description: $- com.hy.search.SearchApplication -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/11/18 13:23
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/11/18 13:23
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@MapperScan(basePackages = "com.hy.search.mapper")
@ComponentScan(basePackages = {"com.hy"})
@EnableDiscoveryClient
@SpringBootApplication
//@EnableCircuitBreaker
//@EnableFeignClients(basePackages = {"com.hy.auth"})
////@EnableFeignClients(
////        clients = {
////                IAnnouncementService.class,
////                ICarouselService.class,
////                IClassificationService.class,
////                AuthService.class
////        }
////)
public class SearchApplication {
    public static void main(String[] args) {
        SpringApplication.run(SearchApplication.class, args);
    }
}
