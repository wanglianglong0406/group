package com.hy.manager;

import com.hy.account.service.IAccountService;
import com.hy.center.service.ICenterStageService;
import com.hy.order.service.IOrderService;
import com.hy.user.service.IUserService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2021/1/29 22:40
 * @UpdateUser: 寒夜
 * @UpdateDate: 2021/1/29 22:40
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@MapperScan(basePackages = "com.hy.manager.mapper")
@ComponentScan(basePackages = {"com.hy"})
@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients(
        clients = {
                IAccountService.class,
                IUserService.class,
                ICenterStageService.class,
                IOrderService.class
        }
)
public class ManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManagerApplication.class,args);
    }
}
