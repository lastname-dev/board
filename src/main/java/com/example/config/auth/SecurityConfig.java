//package com.example.config.auth;
//
//import com.example.user.Role;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
//import org.springframework.security.web.SecurityFilterChain;
//
//@RequiredArgsConstructor
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    @Bean
//    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//
//       // http.csrf().disable()
//        //        .authorizeRequests()
//         //       .antMatchers("**/posts/**/*kind=manager*").hasRole(Role.ADMIN.name())
//          //      .anyRequest().authenticated();
//
//        return http.build();
//    }
//
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return web -> {
//            web.ignoring()
//                    .antMatchers(
//                            "**/","**/users/login","**/users/join",
//                            "/images/**",
//                            "/js/**",
//                            "/css/**"
//                    );
//        };
//    }
//}
