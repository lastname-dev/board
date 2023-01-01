package com.example.board.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true
)
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf().disable();

        http.authorizeRequests()
                .antMatchers("/loginForm", "/users/login", "/", "/users").permitAll()
                .anyRequest().authenticated();

        http.formLogin()
                .loginPage("/loginForm")// 로그인폼 등록, 기존 시큐리티의 로그인폼이 아닌 다른 폼을 사용하겠다
                .usernameParameter("email")
                .passwordParameter("password")
                .loginProcessingUrl("/users/login") // 해당 url로 요청이 들어오면 시큐리티가 대신 로그인 진행을 하도록 위임, 시큐리티 세션 등록이 가능하리라 보임
                .defaultSuccessUrl("/");// 성공하면 메인으로 이동

        //로그아웃은 퍼옴 (https://velog.io/@gmtmoney2357/%EC%8A%A4%ED%94%84%EB%A7%81-%EC%8B%9C%ED%81%90%EB%A6%AC%ED%8B%B0-Logout-%EA%B8%B0%EB%8A%A5)
        http.logout() // 로그아웃 기능 작동함
                .logoutUrl("/users/logout") // 로그아웃 처리 URL, default: /logout, 원칙적으로 post 방식만 지원
                .logoutSuccessUrl("/"); // 로그아웃 성공 후 이동페이지

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> {
            web.ignoring()
                    .antMatchers(
                            "/images/**",
                            "/js/**",
                            "/css/**"
                    );
        };
    }
}
