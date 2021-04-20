package com.hy.order;


import com.hy.center.service.ICenterStageService;
import com.hy.task.annotation.EnableElasticJob;
import com.hy.user.service.IUserService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Description: $- UserApplication -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/11/17 14:01
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/11/17 14:01
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
// 扫描 mybatis 通用 mapper 所在的包
@MapperScan(basePackages = "com.hy.order.mapper")
@ComponentScan(basePackages = {"com.hy"})
@EnableDiscoveryClient
@SpringBootApplication
@EnableElasticJob
@EnableFeignClients(
        clients = {
                IUserService.class,
                ICenterStageService.class

        }
)
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class,args);
    }
}

