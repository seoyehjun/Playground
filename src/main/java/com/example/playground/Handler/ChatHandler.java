package com.example.playground.Handler;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.Map;


@Component
@Log4j2
public class ChatHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> sessions = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String roomCode = getRoomCode(session);
        sessions.put(session.getId(), session);
        session.sendMessage(new TextMessage("Welcome to the chat room: " + roomCode));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String roomCode = getRoomCode(session);
        for (WebSocketSession s : sessions.values()) {
            if (s.isOpen() && getRoomCode(s).equals(roomCode)) {
                s.sendMessage(message);
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session.getId());
    }

    private String getRoomCode(WebSocketSession session)
    {
        String query = session.getUri().getQuery();
        System.out.println("핸들러 내에서 추출한 룸코드 : "+ query.split("roomCode=")[1]);
        if (query != null && query.contains("roomCode="))
        {
            return query.split("roomCode=")[1];
        }
        return null;
    }
}
