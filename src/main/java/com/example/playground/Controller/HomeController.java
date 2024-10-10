package com.example.playground.Controller;

import com.example.playground.Config.Auth.PrincipalDetail;
import com.example.playground.Config.UserInfo.UserRole;
import com.example.playground.Model.Member;
import com.example.playground.Repository.MemberRepository;
import com.example.playground.Service.MailService;
import com.example.playground.Service.MemberService;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;

@Controller
public class HomeController {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MailService mailservice;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @GetMapping("loginForm")
    public String loginForm(Model model)
    {
        return "thymeleaf/loginForm";
    }

    @GetMapping("main")
    public String tomain(Model model)
    {
        return "thymeleaf/main";
    }

    @GetMapping("myinfo")
    public String myinfo(Model model, Authentication authentication, @RequestParam(required = false)String issuccess)
    {
        PrincipalDetail principalDetail = (PrincipalDetail) authentication.getPrincipal();
        Member member = principalDetail.getMember();
        model.addAttribute("member", member);
        model.addAttribute("issuccess",issuccess);
        return "thymeleaf/myinfo";
    }

    @GetMapping("hello")
    public String home(Model model)
    {
        return "hello";
    }

    @GetMapping("/changePassword")
    public String changePassword()
    {
        return "thymeleaf/changePassword";
    }

    @PostMapping("/updatePassword")
    public String updatePassword(@RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword1")String newPassword1,
                                 @RequestParam("newPassword2")String newPassword2,
                                 Authentication authentication,RedirectAttributes ra)
    {
        PrincipalDetail principalDetail = (PrincipalDetail)authentication.getPrincipal();
        Member member = principalDetail.getMember();
        if(!newPassword1.equals(newPassword2))
        {
            ra.addAttribute("issuccess", "두 비밀번호가 다릅니다.");
            return "redirect:/myinfo";
        }
        if( member != null && bCryptPasswordEncoder.matches(currentPassword, member.getUserpw())
        && newPassword1.equals(newPassword2) )
        {
            String encPassword = bCryptPasswordEncoder.encode(newPassword1);
            member.setUserpw(encPassword);
            memberRepository.save(member);
            ra.addAttribute("issuccess","비밀번호 변경 성공");
            return "redirect:/myinfo";
        }

        ra.addAttribute("issuccess","기존 비밀번호를 확인하세요");
        return "redirect:/myinfo";
    }

    //바로 joinForm으로 넘어가는게 아닌 이메일 인증으로 넘어가게 설정해보자
    //이메일 인증을 마치면 joinForm.html로 넘어갈 수 있다.
    @PostMapping("joinForm")
    public String joinForm(Model model, @RequestParam("user_email") String user_email)
    {
        System.out.println("------------user_email" + user_email);
        model.addAttribute("user_email", user_email);
        return "thymeleaf/joinForm";
    }

    @PostMapping("/joinProc")
    public String join(Member member) {
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
    public @ResponseBody String info() {return "Secured 어노테이션 테스트 (ROLE_ADMIN)";}

    @GetMapping("/admin")
    public @ResponseBody String admin_page() {
        return "admin페이지 입니다";
    }

    @GetMapping("/manager")
    public @ResponseBody String manager_page() {
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
    ) {
        System.out.println("/test/login =====================");
        PrincipalDetail principalDetail = (PrincipalDetail) authentication.getPrincipal();
        System.out.println("authentication : " + principalDetail.getMember());

        return "세션 정보 확인하기";
    }

    @GetMapping("/test/oauthlogin")
    public @ResponseBody String testOAuthLogin(
            //Authentication authentication,
            @AuthenticationPrincipal OAuth2User oauth) {
        System.out.println("/test/login =====================");
        //OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        //System.out.println("authentication : "+oauth2User.getAttributes());
        System.out.println("oauth2User : " + oauth.getAttributes());

        return "OAuth용세션 정보 확인하기";
    }

    @GetMapping
    public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetail principaldetail) {
        //System.out.print("principaldetail: "+principaldetail.getMember());
        return "user";
    }


    @GetMapping("/email_verification")
    public String MailPage() {
        return "thymeleaf/email_verification";
    }

    @ResponseBody
    @PostMapping("/mail")
    public String MailSend(String mail) {
        int number = mailservice.sendMail(mail);

        String num = "" + number;

        return num;
    }

    @PostMapping("/updateMember")
    public String updateMember(@RequestParam("nickname") String nickname,
                               @RequestParam("loginId") String loginId,
                               @RequestParam("email") String email,
                               @RequestParam("profileImage") MultipartFile file,
                               Authentication authentication,
                               Model model) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            System.out.println("update 컨트롤러 이동 완료");

            PrincipalDetail principalDetail = (PrincipalDetail) authentication.getPrincipal();//로그인사용자기본키
            int current_user_id = principalDetail.getMember().getId();
            Member member = memberService.findMember(current_user_id);//로그인사용자객체

            String uploadDir = "C:/Temp/iwork/Playground/src/main/resources/static/images/profile";
            String fileName = file.getOriginalFilename();
            System.out.println("파일네임 :"+fileName+":");
            Path filePath = Paths.get(uploadDir, fileName);

            try
            {
                //널체크를해야 빈문자열인지 아닌지 판단하는 isEmpty()를 쓸 수 있다.
                //fileName이 비어있다는것은 myinfo에서 사진 변경이 없었다는 뜻이다.
                //->따라서 이미지를 복붙할필요도 없고, DB의 이미지 경로를 변경할 필요도 없다.
                if(fileName != null && !fileName.isEmpty())
                {
                    //프로젝트 폴더에 이미지 복붙
                    Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                    //DB에는 경로로 이미지 정보 넣는다
                    String profileImageUrl = "/images/profile/" + fileName;

                    member.setProfileImageUrl(profileImageUrl);
                }

                member.setNickname(nickname);
                member.setLoginId(loginId);
                member.setUseremail(email);

                memberService.updateMember(member);//수정된 정보가 있을경우 로그인된 사용자 정보 수정됨
            }
            catch (IOException e)
            {
                System.out.println("try 오류 뜸");
            }

        }

        return "redirect:/main";

    }



}
