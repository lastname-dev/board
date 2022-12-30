package com.example.board.config;

import com.example.board.config.resolver.UserEmailArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final UserEmailArgumentResolver userEmailArgumentResolver;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //registry.addInterceptor(new IdentityVerificationWebInterceptor())
        //      .addPathPatterns("/posts/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(userEmailArgumentResolver);
    }

}