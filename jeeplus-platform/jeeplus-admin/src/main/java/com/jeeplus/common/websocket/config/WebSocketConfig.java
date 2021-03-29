package com.jeeplus.common.websocket.config;

import com.jeeplus.common.websocket.service.system.SystemInfoSocketHandler;
import com.jeeplus.common.websocket.service.system.SystemInfoSocketHandshakeInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebMvc
@EnableWebSocket
public class WebSocketConfig extends WebMvcConfigurerAdapter implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
      //注册 系统通知socket服务
        registry.addHandler(systemInfoSocketHandler(),"/systemInfoSocketServer").addInterceptors(new SystemInfoSocketHandshakeInterceptor());
        registry.addHandler(systemInfoSocketHandler(), "/sockjs/systemInfoSocketServer").addInterceptors(new SystemInfoSocketHandshakeInterceptor())
                .withSockJS();
    }


    @Bean
    public WebSocketHandler systemInfoSocketHandler(){
        return new SystemInfoSocketHandler();
    }
}