package com.example.playground.Controller;

import com.example.playground.Config.Auth.PrincipalDetail;
import com.example.playground.Config.UserInfo.UserRole;
import com.example.playground.Model.Member;
import com.example.playground.Repository.MemberRepository;
import com.example.playground.Service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class HomeController
{
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private  MailService mailservice;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @GetMapping("loginForm")
    public String loginForm(Model model){ return "thymeleaf/loginForm"; }

    @GetMapping("main")
    public String tomain(Model model)
    {
        return "thymeleaf/main";
    }

    @GetMapping("myinfo")
    public String myinfo(Model model, Authentication authentication)
    {
        PrincipalDetail principalDetail = (PrincipalDetail) authentication.getPrincipal();
        Member member = principalDetail.getMember();
        model.addAttribute("member", member);
        return "thymeleaf/myinfo";
    }

    @GetMapping("hello")
    public String home(Model model)
    {
        return "hello";
    }



    //바로 joinForm으로 넘어가는게 아닌 이메일 인증으로 넘어가게 설정해보자
    //이메일 인증을 마치면 joinForm.html로 넘어갈 수 있다.
    @PostMapping("joinForm")
    public String joinForm(Model model, @RequestParam("user_email")String user_email)
    {
        System.out.println("------------user_email"+user_email);
        model.addAttribute("user_email",user_email);
        return "thymeleaf/joinForm";
    }

    @PostMapping("/joinProc")
    public  String join(Member member)
    {
        System.out.println(member);
        member.setRole(UserRole.USER);
        String rawPassword = member.getUserpw();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        member.setUserpw(encPassword);
        memberRepository.save(member);
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
        //System.out.print("principaldetail: "+principaldetail.getMember());
        return "user";
    }


    @GetMapping("/email_verification")
    public String MailPage()
    {
        return "thymeleaf/email_verification";
    }

    @ResponseBody
    @PostMapping("/mail")
    public String MailSend(String mail)
    {
        int number = mailservice.sendMail(mail);

        String num = "" + number;

        return num;
    }


}
