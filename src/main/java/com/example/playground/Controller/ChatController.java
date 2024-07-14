package com.example.playground.Controller;

import com.example.playground.Config.Auth.PrincipalDetail;
import com.example.playground.Model.User;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;


@Controller
@Log4j2
public class ChatController {

    @GetMapping("/chat")
    public String chatGET(Authentication authentication, Model model)
    {
        System.out.println("authentication : "+authentication);
        PrincipalDetail principal = (PrincipalDetail)authentication.getPrincipal();
        model.addAttribute("username", principal.getUsername());
        log.info("@ChatController, chat GET()");
        return "thymeleaf/chater";
    }

    @GetMapping("/template_test")
    public String template_test(Model model)
    {
        // user 객체와 items 리스트를 생성하거나 가져와서 모델에 추가합니다.
        User user = new User(111,"John Doe", "admin@gmail.com","Admin"); // 예시로 User 객체를 생성했습니다.
        List<String> items = Arrays.asList("Item 1", "Item 2", "Item 3");

        model.addAttribute("user", user);
        model.addAttribute("items", items);

        return "thymeleaf/template_test"; // Thymeleaf 템플릿 파일의 이름을 반환합니다.
    }

}