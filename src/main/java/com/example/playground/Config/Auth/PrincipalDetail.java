package com.example.playground.Config.Auth;

//시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 진행시킨다.
//로그인 진행이 완료가 되면 Security session을 만들어준다.

//Security Session은 Authentication 객체를 가지고
//Authentication 객체는 UserDetails(밑의PrincipalDetail) 객체를 가진다

import com.example.playground.Model.Member;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Data
public class PrincipalDetail implements UserDetails, OAuth2User
{

    private Member member;
    private Map<String, Object> attributes;

    //일반 로그인용 생성자
    public PrincipalDetail(Member member)
    {
        this.member = member;
    }

    //OAuth 로그인용 생성자
    public PrincipalDetail(Member member, Map<String, Object> attributes)
    {
        this.member = member;
        this.attributes = attributes;
    }



    //해당 Member의 권한을 리턴하는 곳
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new GrantedAuthority(){
            @Override
            public String getAuthority() {
                return member.getRole();
            }
        });
        return authorities;
    }

    @Override
    public String getPassword() {
        return member.getUserpw();
    }

    @Override
    public String getUsername() {
        return member.getUsername();
    }

    @Override
    public boolean isAccountNonExpired()
    {
        return true;//true로 바꿔줘야한다(만료되지 않음)
    }

    @Override
    public boolean isAccountNonLocked()
    {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired()
    {
        return true;
    }

    @Override
    public boolean isEnabled()
    {
        return true;
    }

    @Override
    public String getName() {
        return null;
    }
    @Override
    public Map<String, Object> getAttributes()
    {
        return attributes;
    }
}
