package com.hy.game;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

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
@MapperScan(basePackages = "com.hy.game.mapper")
@ComponentScan(basePackages = {"com.hy"})
@EnableDiscoveryClient
@SpringBootApplication
//@EnableCircuitBreaker
@EnableScheduling
//@EnableFeignClients(
//        clients = {
//                AuthService.class
//        }
//)
public class GameApplication {
    public static void main(String[] args) {
        SpringApplication.run(GameApplication.class,args);
    }
}

