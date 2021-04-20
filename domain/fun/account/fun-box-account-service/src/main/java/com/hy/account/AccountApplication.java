package com.hy.account;

import com.hy.lottery.service.IBetRecordService;
import com.hy.order.service.IOrderService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/11/19 16:03
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/11/19 16:03
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */

@MapperScan(basePackages = "com.hy.account.mapper")
@ComponentScan(basePackages = {"com.hy"})
@EnableDiscoveryClient
@SpringBootApplication
@EnableCircuitBreaker
@EnableFeignClients(
        clients = {
                IOrderService.class,
                IBetRecordService.class
        }
)
public class AccountApplication {
    public static void main(String[] args) {
        SpringApplication.run(AccountApplication.class,args);
    }
}
