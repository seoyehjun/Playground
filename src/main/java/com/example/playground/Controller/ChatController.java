package com.example.playground.Controller;

import com.example.playground.Config.Auth.PrincipalDetail;
import com.example.playground.Model.User;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;


@Controller
@Log4j2
public class ChatController
{

    @GetMapping("/chatcode")
    public String chatcode()
    {
        return "thymeleaf/chatcode";
    }

    @PostMapping("/createRoom")
    public String createRoom(Authentication authentication, Model model)
    {
        String roomId = UUID.randomUUID().toString();
        //UUID는 범용 고유 식별자(Universally Unique Identifier)의 약자로,
        // 자바에서 고유한 식별자를 생성하는 데 사용됩니다.
        log.info("방생성 확인된 룸아이디는? :  "+roomId);
        log.info("이용자 이름 : "+authentication.getName());
        return "redirect:/chat?roomId=" + roomId;
    }

    @PostMapping("/joinRoom")
    public String joinRoom(Authentication authentication, @RequestParam("roomId") String roomId)
    {
        log.info("방조인 확인된 룸아이디는? :  "+roomId);
        log.info("이용자 이름 : "+authentication.getName());
        return "redirect:/chat?roomId=" + roomId;
    }

    @GetMapping("/chat")
    public String chatGET(Authentication authentication, @RequestParam String roomId, Model model)
    {
        System.out.println("authentication : " + authentication);

        PrincipalDetail principal = (PrincipalDetail) authentication.getPrincipal();
        model.addAttribute("username", principal.getUsername());
        model.addAttribute("member", principal.getMember());
        model.addAttribute("roomId", roomId);
        log.info("@ChatController, chat GET()");
        return "thymeleaf/chater";
    }

}