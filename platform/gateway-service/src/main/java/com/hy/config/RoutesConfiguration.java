package com.hy.config;

import com.hy.filter.WebsocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: $- RoutesConfiguration -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/11/17 1:06
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/11/17 1:06
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Configuration
public class RoutesConfiguration {
    @Autowired
    private KeyResolver hostNameResolver;

    @Autowired
    @Qualifier("redisLimiterUser")
    private RateLimiter rateLimiterUser;


    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder, WebsocketHandler websocketHandler) {
        return builder.routes()

                .route(r -> r.path(
                        "/luckshop/user-api/passport/v1/**", "/luckshop/user-api/user-center/v1/**","/luckshop/user-api/v1/**")
                        //.filters(f -> f.filter(authFilter))
                        .uri("lb://LUCK-SHOP-USER-SERVICE")
                )

                .route(r -> r.path("/luckshop/order-api/v1/**")
                        .uri("lb://LUCK-SHOP-ORDER-SERVICE")
                )

                .route(r -> r.path("/luckshop/center-stage-api/v1/**")
                        .uri("lb://LUCK-SHOP-CENTER-STAGE-SERVICE")
                )

                .route(r -> r.path("/luckshop/account-api/v1/**","/luckshop/account-api/bank-info/v1/**")
                        .uri("lb://LUCK-SHOP-ACCOUNT-SERVICE")
                )

                .route(r -> r.path("/manager/items-api/v1/**","/manager/order-management-center/v1/**"
                        ,"/manager/passport-api/v1/**","/manager/user-management-center/v1/**","/manager/sys-management/v1/**")
                        .uri("lb://MANAGER-SERVICE")
                )





                .route(r -> r.path(
                        "/user-api/v1/**", "/user-api/sign/v1/**","/user-api/feedback/v1/**")
                        .uri("lb://FUN-BOX-USER-SERVICE")
                )


                .route(r -> r.path("/search-api/classification/v1/**",
                        "/search-api/carousel/v1/**",
                        "/search-api/announcement/v1/**",
                        "/search-api/notification/v1/**",
                        "/search-api/price-info/v1/**",
                        "/search-api/platfrom-info/v1/**",
                        "/earch-api/vip/v1/**")
                        .uri("lb://FUN-BOX-SEARCH-SERVICE")
                )

                .route(r -> r.path(
                        "/account-api/v1/**", "/account-api/bankinfo/v1/**")
                        .uri("lb://FUN-BOX-ACCOUNT-SERVICE")
                )

                .route(r -> r.path(
                        "/order-api/v1/**")
                        .uri("lb://FUN-BOX-ORDER-SERVICE")
                )
                .route(r -> r.path(
                        "/lottery-api/**")
                        .uri("lb://FUN-BOX-LOTTERY-SERVICE")
                )

                .route(r -> r.path(
                        "/webSocket/**")
                        .filters(f -> f.filter(websocketHandler))
                        .uri("lb://FUN-BOX-LOTTERY-SERVICE")
                )

                .build();

    }
}
