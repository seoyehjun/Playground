package com.example.playground.Config.Auth;

import com.example.playground.Model.Member;
import com.example.playground.Repository.MemberRepository;
import com.example.playground.Service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


// 시큐리티 설정에서 loginProcessingUrl("/login"); 설정을 해놨기 때문에
// /login 요청이 오면 자동으로 UserDetailsService 타입으로 IoC되어 있는 loadUserByUsername
// 함수가 실행 된다.
@Service
public class PrincipalDetailsService implements UserDetailsService
{
    @Autowired
    private final MemberRepository memberRepository;

    @Autowired
    MemberService memberService;

    public PrincipalDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // 이 함수는 loginForm에서 로그인버튼 클릭하면 실행되는 함수임
    // loginForm에서 username이라는 name속성을 가진것에 대응된다.
    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException
    {
        Member memberEntity = memberService.optionaltogeneral(memberRepository.findByLoginId(loginId));
        if(memberEntity != null)//멤버가 존재하면 properties에 정해놓은 아이디 비번 쓸모없다.
        {
            return new PrincipalDetail(memberEntity);
            //여기서 리턴된 UserDetail(PrincipalDetail)은 Authentication
            //내부에 쏙들어가게 된다.(PrincialDetail필기 참조)
        }
        return null; //로그인 실패
    }
}
