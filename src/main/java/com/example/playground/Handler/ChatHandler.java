package com.example.playground.Handler;

import com.example.playground.Config.Auth.PrincipalDetail;
import com.example.playground.Model.Member;
import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.JsonObject;
import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
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
public class ChatHandler extends TextWebSocketHandler
{
    private static Map<String, List<WebSocketSession>> roomSessions = new HashMap<>();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception
    {
        String payload = message.getPayload();
        log.info("JSON 테스트 위치 payload : " + payload);
        String roomId = (String) session.getAttributes().get("roomId");

        Gson gson = new Gson();
        JsonObject jsonPayload = gson.fromJson(payload, JsonObject.class);
        String username = jsonPayload.get("username").getAsString();
        String userMessage = jsonPayload.get("message").getAsString();
        String profileImgUrl = getProfileImageUrl(session);

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("username", username);
        jsonResponse.put("message", userMessage);
        jsonResponse.put("profileImgUrl",profileImgUrl);

        if (roomId != null && roomSessions.containsKey(roomId))
        {
            for (WebSocketSession sess : roomSessions.get(roomId))
            {
                sess.sendMessage(new TextMessage(jsonResponse.toString()));
            }
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception
    {
        String roomId = (String) session.getAttributes().get("roomId");
        roomSessions.computeIfAbsent(roomId, k -> new CopyOnWriteArrayList<>()).add(session);
        log.info(session + " 클라이언트 접속, 방코드: " + roomId);

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("message","방코드: "+roomId);
        jsonResponse.put("username","::: 운영 :::");
        session.sendMessage(new TextMessage(jsonResponse.toString()));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception
    {
        String roomId = (String) session.getAttributes().get("roomId");
        if (roomId != null && roomSessions.containsKey(roomId))
        {
            roomSessions.get(roomId).remove(session);
            if (roomSessions.get(roomId).isEmpty())
            {
                roomSessions.remove(roomId);
            }
        }
        log.info(session + " 클라이언트 접속 해제");
    }

    private String getProfileImageUrl(WebSocketSession session)
    {
        Authentication authentication = (Authentication)session.getAttributes().get("authentication");
        PrincipalDetail principalDetail = (PrincipalDetail)authentication.getPrincipal();
        Member member = principalDetail.getMember();

        if (member.getProfileImageUrl() != null)
        {
            return member.getProfileImageUrl(); // "picture"는 Google OAuth2의 경우 프로필 이미지 URL을 나타냅니다.
        }
        else
        {
            return "/images/profile/default_profile_image.png"; // 기본 프로필 이미지 경로 설정
        }
    }

}
