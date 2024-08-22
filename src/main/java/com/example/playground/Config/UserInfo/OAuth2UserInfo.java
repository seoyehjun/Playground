package com.example.playground.Config.UserInfo;

//모든 사이트의 Info를 받을 수 있는 인터페이스 입니다
//PrincipalOauth2UserService에서 사용해서 사용자의 정보를 받을 예정입니다
public interface OAuth2UserInfo
{
    String getProviderId();
    String getProvider();
    String getEmail();
    String getName();
}