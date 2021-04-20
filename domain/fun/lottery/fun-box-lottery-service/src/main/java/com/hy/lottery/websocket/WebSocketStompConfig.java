package com.hy.lottery.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/11/27 8:55
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/11/27 8:55
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */

@Configuration
// 启用STOMP消息, 表明这个配置类不仅配置了WebSocket， 还配置了基于代理的STOMP消息
@EnableWebSocketMessageBroker
public class WebSocketStompConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //注册一个Stomp 协议的endpoint指定URL为webSocket,并用.withSockJS()指定 SockJS协议。
        // .setAllowedOrigins("*")设置跨域
        registry.addEndpoint("/webSocket")
                .setAllowedOrigins("*")
                .withSockJS();
    }
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        //配置消息代理(message broker)
        //将消息传回给以‘/topic’开头的客户端
        config.enableSimpleBroker("/topic");

    }



}



