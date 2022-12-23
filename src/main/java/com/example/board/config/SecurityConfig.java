package com.example.board.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf().disable();

        http.authorizeRequests()
                .antMatchers("/postsForm").hasAuthority("ROLE_USER")
                .antMatchers("/posts*").hasAuthority("ROLE_USER")
                .antMatchers("/users/loginForm", "/").permitAll();

        http.formLogin()
                .loginPage("/users/loginForm")// 로그인폼 등록, 기존 시큐리티의 로그인폼이 아닌 다른 폼을 사용하겠다
                .usernameParameter("email")
                .passwordParameter("password")
                .loginProcessingUrl("/users/login") // 해당 url로 요청이 들어오면 시큐리티가 대신 로그인 진행을 하도록 위임, 시큐리티 세션 등록이 가능하리라 보임
                .defaultSuccessUrl("/");// 성공하면 메인으로 이동

        //로그아웃은 퍼옴 (https://velog.io/@gmtmoney2357/%EC%8A%A4%ED%94%84%EB%A7%81-%EC%8B%9C%ED%81%90%EB%A6%AC%ED%8B%B0-Logout-%EA%B8%B0%EB%8A%A5)
        http.logout() // 로그아웃 기능 작동함
                .logoutUrl("/users/logout") // 로그아웃 처리 URL, default: /logout, 원칙적으로 post 방식만 지원
                .logoutSuccessUrl("/"); // 로그아웃 성공 후 이동페이지

        /*
        일단 위와 같이 구성하면 로그인, 로그아웃에 대한 서비스를 구현할 필요가 없음
        즉, 현재 UserController에 있는 로그인 로그아웃 메소드는 필요가 없음
        또한 시큐리티 내부에 있는 자체 세션을 가지고 인증을 진행함

        다만, 현재 발견된 문제는 다음과 같음
        1. .formLogin() 다음에 .loginPage를 넣어서 우리가 만든 프론트를 사용할 경우 PricipalDetailService에 값이 들어가지 않음
            따라서 User user가 null이 되고 로그인이 되지 않음
        2. 위에서 진행하는 로그인, 로그아웃은 원칙적으로 post를 지원함
        3. 스프링 시큐리티가 login, logout 로직을 가로채가기 때문에 우리가 컨트롤러단에서 설정한 세션값은 적용되지않음, 애초에 메소드가 실행도 안됨
        -> 끝나면 GET으로 실행할순 있음
        * */

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
