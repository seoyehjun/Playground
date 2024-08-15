package com.example.playground.Config.oauth;

import com.example.playground.Config.Auth.PrincipalDetail;
import com.example.playground.Model.Member;
import com.example.playground.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService
{
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserRepository userRepository;

    //구글로 부터 받은 userRequest 데이터에 대한 후처리되는 함수
    // OAuth2User는 PrincipalDetail과 대응된다.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException
    {
        System.out.println("getClientRegistration : " + userRequest.getClientRegistration().getRegistrationId());//registrationID로 어떤 OAuth로 로그인 했는지 확인 가능.
        System.out.println("getAccessToken : " + userRequest.getAccessToken());
        //구글 로그인 버튼 클릭 -> 구글 로그인창 -> 로그인을 완료 -> code를 리턴(OAuth-Client라이브러리가 code를 받는다) -> AccessToken요청
        //userRequest 정보 받는다-> userRequest를 통해서loadUser함수 호출 -> 구글로부터 회원프로필 받아준다.

        OAuth2User oAuth2User = super.loadUser(userRequest);

        System.out.println("getAttributes : " + super.loadUser(userRequest).getAttributes());
        //위문장 출력 결과: getAttributes : {sub=116875832835056222762, name=권기한, given_name=기한, family_name=권,
        // picture=https://lh3.googleusercontent.com/a/ACg8ocLHTWu96T-YLMLFfE6Eyo8YVJtFvVmvaNhwFvI9LrdeFMcfeL6i=s96-c,
        // email=skgy322@gmail.com, email_verified=true}

        System.out.println("userRequest.getClientRegistration().getRegistrationId()" + userRequest.getClientRegistration().getRegistrationId());
        //위문장 출력 결과: google

        System.out.println("userRequest.getClientRegistration().getClientId()" + userRequest.getClientRegistration().getClientId());
        //위문장 출력 결과: 281351119419-61n6htsd2aqkn7p3lb8052h6f50ls97a.apps.googleusercontent.com

        String provider = userRequest.getClientRegistration().getClientId();
        String providerId = oAuth2User.getAttribute("sub");
        String username = oAuth2User.getAttribute("name");//google_12312312312 format
        String password = bCryptPasswordEncoder.encode("의미업다");
        String email = oAuth2User.getAttribute("email");
        String role = "ROLE_USER";

        Member userEntity = userRepository.findByUsername(username);

        if(userEntity == null)//DB에 유저정보 없을시
        {
            userEntity = Member.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userRepository.save(userEntity);
        }

        //반환된게 Authentication객체에 들어가게 된다.
        return new PrincipalDetail(userEntity, oAuth2User.getAttributes());
    }

}
