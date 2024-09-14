package com.example.playground.Config;

import com.example.playground.Handler.ChatHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;


// @RequiredArgsConstructor어노테이션은 클래스에 선언된 final 변수들,
// 필드들을 매개변수로 하는 생성자를 자동으로 생성해주는 어노테이션입니다
@Configuration
@RequiredArgsConstructor
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer
{

    private final ChatHandler chatHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry)
    {
        registry.addHandler(chatHandler, "ws/chat").setAllowedOrigins("*");
    }
}
