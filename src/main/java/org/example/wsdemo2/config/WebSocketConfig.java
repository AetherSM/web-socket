package org.example.wsdemo2.config;

import org.example.wsdemo2.handler.MyWebSocketHandler;
import org.example.wsdemo2.interceptor.MyWsInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Autowired
    private MyWebSocketHandler myWebSocketHandler;
    @Autowired
    private MyWsInterceptor myWsInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(myWebSocketHandler, "/ws") // WebSocket请求的路径
                .addInterceptors(myWsInterceptor) // 可以加入会话拦截器
                .setAllowedOrigins("*"); // 允许跨域
    }
}