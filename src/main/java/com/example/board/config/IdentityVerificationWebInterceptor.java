package com.example.board.config;

import com.example.board.config.auth.PrincipalDetails;
import com.example.board.model.post.PostDto;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StreamUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

//수정과 삭제에 대한
public class IdentityVerificationWebInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 스프링 시큐리티 세션값
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        // 요청 값
        ServletInputStream servletInputStream = request.getInputStream();
        String body = StreamUtils.copyToString(servletInputStream, StandardCharsets.UTF_8);

        EmailData emailData = new ObjectMapper().readValue(body, EmailData.class);

        return emailData.userEmail.equals(principalDetails.getUsername());
    }

    private static class EmailData {
        String userEmail;
    }
}
