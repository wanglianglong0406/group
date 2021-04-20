package com.hy.user;

import com.hy.account.service.IAccountService;
import com.hy.order.service.IOrderService;
import com.hy.search.service.INotificationService;
import com.hy.search.service.IVipService;
import com.hy.task.annotation.EnableElasticJob;
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
@MapperScan(basePackages = "com.hy.user.mapper")
@ComponentScan(basePackages = {"com.hy"})
@EnableDiscoveryClient
@SpringBootApplication
//@EnableCircuitBreaker
@EnableElasticJob
@EnableFeignClients(
        clients = {
                IAccountService.class,
                IOrderService.class,
                IVipService.class,
                INotificationService.class
        }
)
public class UserApplication {
    //http://localhost:10002/swagger-ui.html
    //http://q37av6.natappfree.cc/doc.html
    //http://q37av6.natappfree.cc/swagger-ui.html
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class,args);
    }
}

