package com.example.playground.Service;

import com.example.playground.Config.Auth.PrincipalDetail;
import com.example.playground.Model.Member;
import com.example.playground.Repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemberService
{
    @Autowired
    private MemberRepository memberRepository;

    public Member optionaltogeneral(Optional<Member> optionalMember) {
        // Optional 객체가 비어있을 경우 기본값을 설정하거나 예외를 던질 수 있습니다.
        return optionalMember.orElseThrow(() -> new RuntimeException("Member not found"));
    }

    public Member findMember(int id)
    {
        return optionaltogeneral(memberRepository.findById(id));
    }

    public PrincipalDetail updateMember(Member member)
    {
        memberRepository.save(member);

        //사용자 인증정보가 중간에 변경되었을경우 리턴값으로 업데이트 가능
        return new PrincipalDetail(member);
    }


}
