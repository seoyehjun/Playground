package com.example.playground.Service;

import com.example.playground.Model.Member;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemberService
{
    public Member optionaltogeneral(Optional<Member> optionalMember) {
        // Optional 객체가 비어있을 경우 기본값을 설정하거나 예외를 던질 수 있습니다.
        return optionalMember.orElseThrow(() -> new RuntimeException("Member not found"));
    }

}
