package com.example.playground.Handler;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@Log4j2
public class ChatHandler extends TextWebSocketHandler {
    private static Map<String, List<WebSocketSession>> roomSessions = new HashMap<>();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("payload : " + payload);
        String roomId = (String) session.getAttributes().get("roomId");

        if (roomId != null && roomSessions.containsKey(roomId)) {
            for (WebSocketSession sess : roomSessions.get(roomId)) {
                sess.sendMessage(message);
            }
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String roomId = (String) session.getAttributes().get("roomId");
        roomSessions.computeIfAbsent(roomId, k -> new CopyOnWriteArrayList<>()).add(session);
        log.info(session + " 클라이언트 접속, 방코드: " + roomId);
        session.sendMessage(new TextMessage("방코드: " + roomId));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String roomId = (String) session.getAttributes().get("roomId");
        if (roomId != null && roomSessions.containsKey(roomId)) {
            roomSessions.get(roomId).remove(session);
            if (roomSessions.get(roomId).isEmpty()) {
                roomSessions.remove(roomId);
            }
        }
        log.info(session + " 클라이언트 접속 해제");
    }
}
