package com.example.playground.Config;

import com.example.playground.Handler.ChatHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;


//보통 DI(의존성 주입)을 방식에는 필드 주입(Field Injection),
// 수정자 주입(Setter Injection), 생성자 주입(Constructor Injection)의
// 3가지의 방법이 있는데 이중에서 가장 권장하는 의존성 주입은 생성자 주입 방식입니다.

// @RequiredArgsConstructor어노테이션은 클래스에 선언된 final 변수들,
// 필드들을 매개변수로 하는 생성자를 자동으로 생성해주는 어노테이션입니다
@Configuration
@RequiredArgsConstructor
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final ChatHandler chatHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatHandler, "ws/chat").setAllowedOrigins("*");
    }
}
