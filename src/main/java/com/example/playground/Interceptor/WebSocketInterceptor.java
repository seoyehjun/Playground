package com.example.playground.Interceptor;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

public class WebSocketInterceptor implements HandshakeInterceptor
{

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception
    {

        System.out.println("WebSocketInterceptor 실행됨(beforHandshake)");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String roomId = request.getURI().getPath().split("/ws/chat/")[1];

        attributes.put("roomId", roomId);
        attributes.put("authentication",authentication);
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception)
    {

    }
}
