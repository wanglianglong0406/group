package com.hy.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collections;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/12/1 22:36
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/12/1 22:36
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Component
@Slf4j
public class WebsocketHandler implements Ordered, GatewayFilter {

    private final static String DEFAULT_FILTER_PATH = "/webSocket/info";

//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        String upgrade = exchange.getRequest().getHeaders().getUpgrade();
//        log.debug("Upgrade : {}", upgrade);
//        URI requestUrl = exchange.getRequiredAttribute(GATEWAY_REQUEST_URL_ATTR);
//        log.debug("path: {}", requestUrl.getPath());
//        String scheme = requestUrl.getScheme();
//        if (!"ws".equals(scheme) && !"wss".equals(scheme)) {
//            return chain.filter(exchange);
//        } else if (DEFAULT_FILTER_PATH.equals(requestUrl.getPath())) {
//            String wsScheme = convertWsToHttp(scheme);
//            URI wsRequestUrl = UriComponentsBuilder.fromUri(requestUrl).scheme(wsScheme).build().toUri();
//            exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, wsRequestUrl);
//        }
//        return chain.filter(exchange);
//    }


    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 2;
    }

    static String convertWsToHttp(String scheme) {
        scheme = scheme.toLowerCase();
        return "ws".equals(scheme) ? "http" : "wss".equals(scheme) ? "https" : scheme;
    }


    @Override
    public Mono<Void> filter(ServerWebExchange ctx, GatewayFilterChain chain) {
        ServerHttpRequest request = ctx.getRequest();
        if (CorsUtils.isCorsRequest(request)) {
            ServerHttpResponse response = ctx.getResponse();
            HttpHeaders headers = response.getHeaders();
            log.debug(" origin : {}", request.getHeaders().get(HttpHeaders.ORIGIN).get(0));
            headers.setAccessControlAllowOrigin((request.getHeaders().get(HttpHeaders.ORIGIN)).get(0));
            headers.setAccessControlAllowCredentials(true);
            headers.setAccessControlMaxAge(Integer.MAX_VALUE);
            headers.setAccessControlAllowHeaders(Collections.singletonList("*"));
            headers.setAccessControlAllowMethods(Arrays.asList(HttpMethod.OPTIONS,
                    HttpMethod.GET, HttpMethod.HEAD, HttpMethod.POST,
                    HttpMethod.DELETE, HttpMethod.PUT));
            if (request.getMethod() == HttpMethod.OPTIONS) {
                response.setStatusCode(HttpStatus.OK);
                return Mono.empty();
            }
        }
        return chain.filter(ctx);
    }
}
