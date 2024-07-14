package com.example.playground.Config;

import com.example.playground.Config.Auth.PrincipalDetailsService;
import com.example.playground.Config.oauth.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig
{
    @Autowired
    PrincipalOauth2UserService principalOauth2UserService;
/*
    @Bean
    public BCryptPasswordEncoder encodePwd()
    {
        return new BCryptPasswordEncoder();
    }
*/
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, PrincipalDetailsService principalDetailsService) throws Exception
    {
        System.out.println("실행은 되냐************************");
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/login","/hello","/login/joinForm","/error").permitAll()
                        .requestMatchers("/weather/getweather").authenticated()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/manager/**").hasAnyRole("MANAGER", "ADMIN")
                        .anyRequest().permitAll()

                )
                .formLogin(form -> form
                        .loginPage("/loginForm")
                        .loginProcessingUrl("/loginProc")//login주소가 호출되면 시큐리티가 낚아채서 대신 로그인 진행
                        .defaultSuccessUrl("/hello")
                        .usernameParameter("username")
                        .passwordParameter("userpw")
                        .successHandler(successHandler())
                        .permitAll())//form 태그 안의 input태그의 name속성을 의미
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/loginForm")
                        .userInfoEndpoint(endpoint-> endpoint.userService(principalOauth2UserService))//구글 로그인 완료후 후처리
                        .successHandler(successHandler())
                );

        return http.build();
    }
    @Bean
    public AuthenticationSuccessHandler successHandler() {
        SimpleUrlAuthenticationSuccessHandler handler = new SimpleUrlAuthenticationSuccessHandler();
        handler.setUseReferer(true);
        return handler;
    }

}


