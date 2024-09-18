package com.example.playground.Repository;

import com.example.playground.Model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//CRUD 함수를 JpaRepository가 들고 있다.
//@Repository라는 어노테이션이 없어도 IoC된다. 이유는 JpaRepository를 상속했기 때문에
public interface MemberRepository extends JpaRepository<Member, Integer>
{
    //findBy까지는 고정으로 쓰고
    //user_name은 원하는 열
    //select * from member where user_name = ?;이런식으로 실행됨
    public Member findByNickname(String username);


    Optional<Member> findByLoginId(String loginId);
}
