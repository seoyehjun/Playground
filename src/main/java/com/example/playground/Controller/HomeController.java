package com.example.playground.Controller;

import com.example.playground.Config.Auth.PrincipalDetail;
import com.example.playground.Model.Member;
import com.example.playground.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

@Controller
@RequestMapping("/")
public class HomeController
{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("hello")
    public String home(Model model)
    {
        return "hello";
    }


    @GetMapping("loginForm")
    public String login(Model model)
    {
        return "login/loginForm";
    }

    @GetMapping("joinForm")
    public String joinForm(Model model)
    {
        return "login/joinForm";
    }

    @PostMapping("/joinProc")
    public  String join(Member member)
    {
        System.out.println(member);
        member.setRole("ROLE_USER");
        String rawPassword = member.getUserpw();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        member.setUserpw(encPassword);
        userRepository.save(member);
        return "redirect:/loginForm";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/info")
    public @ResponseBody String info()
    {
        return "Secured 어노테이션 테스트 (ROLE_ADMIN)";
    }

    @GetMapping("/admin")
    public @ResponseBody String admin_page()
    {
        return "admin페이지 입니다";
    }
    @GetMapping("/manager")
    public @ResponseBody String manager_page()
    {
        return "manager페이지 입니다";
    }



    /* ----- 이밑은 사용자 정보를 받아오는 법을 정리한 것 들입니다 ----- */

    @GetMapping("/test/login")
    public @ResponseBody String loginTest(//Authentication 객체 DI(의존성 주입해줌)
            Authentication authentication,
           @AuthenticationPrincipal UserDetails userDetails
                                          //getPrincipal()의 결과로 principalDetail가져온다
                                          //PrincipalDetail은 UserDetails를 상속받음으로
                                          //위처럼 써놓으면 PrincipalDetail자동으로 받는다.
    )
    {
        System.out.println("/test/login =====================");
        PrincipalDetail principalDetail = (PrincipalDetail)authentication.getPrincipal();
        System.out.println("authentication : "+principalDetail.getMember());

        return "세션 정보 확인하기";
    }
    @GetMapping("/test/oauthlogin")
    public @ResponseBody String testOAuthLogin(
            //Authentication authentication,
            @AuthenticationPrincipal OAuth2User oauth)
    {
        System.out.println("/test/login =====================");
        //OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        //System.out.println("authentication : "+oauth2User.getAttributes());
        System.out.println("oauth2User : "+oauth.getAttributes());

        return "OAuth용세션 정보 확인하기";
    }

    @GetMapping
    public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetail principaldetail)
    {
        System.out.print("principaldetail: "+principaldetail.getMember());
        return "user";
    }



}
