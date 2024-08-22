/*package com.example.playground.Config.oauth;

import com.example.playground.Model.Member;

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


org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean
with name 'principalDetailsService' defined in file [C:\Temp\iwork\Playground\build\cl
asses\java\main\com\example\playground\Config\Auth\PrincipalDetailsService.class]: Unsat
isfied dependency expressed through constructor parameter 0: Error creating bean with name
'userRepository' defined in com.example.playground.Repository.UserRepository defined in
@EnableJpaRepositories declared on JpaRepositoriesRegistrar.EnableJpaRepositoriesConfiguration:
Could not create query for public abstract com.example.playground.Model.Member com.example.playground
        .Repository.UserRepository.findByUsername(java.lang.String); Reason: Failed to create query for
method public abstract com.example.playground.Model.Member com.example.playground.Repository.UserRepositor
y.findByUsername(java.lang.String); No property 'username' found for type 'Member'
*/