package com.example.board.controller;

import com.example.board.BaseTest;
import com.example.board.config.auth.PrincipalDetails;
import com.example.board.model.post.Kind;
import com.example.board.model.post.Post;
import com.example.board.model.post.PostDto;
import com.example.board.model.user.Gender;
import com.example.board.model.user.Role;
import com.example.board.model.user.User;
import com.example.board.model.user.userDto.JoinRequestDto;
import com.example.board.repository.PostRepository;
import com.example.board.repository.UserRepository;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;


import java.util.logging.Logger;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class PostControllerTest extends BaseTest {


    @BeforeEach
    void setUp() throws Exception {
        User user = new User(joinProc());
        PrincipalDetails userDetails = new PrincipalDetails(user);
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities()));

        mockMvc.perform(post("https://localhost:8080/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(joinProc())))
                .andExpect(status().isOk());
        //회원가입
    }
    @Autowired
    private ObjectMapper objectMapper;

    String url = "https://localhost:8080/posts";

    @Autowired
    UserRepository userRepository;
    @Autowired
    PostRepository postRepository;

//    @BeforeEach
//    void init(){
//        JoinRequestDto joinRequestDto = joinProc();
//    }


    @Test
    void viewBoard() {
        //given

    }

    @Test
    void read() {

    }

    @Test
    @Order(100)
    void write() throws Exception {
        //given

        PostDto input = PostDto.builder().
            kind(Kind.NOTICE).
                title("title1").
        content("content1").build();


        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk());

        Post post = postRepository.findByTitle("title1");

        assertThat(post.getTitle()).isEqualTo("title1");

    }

    @Test
    @Order(200)
    void deleteTest() throws Exception {
        write();

        Logger logger = Logger.getLogger("mylogger");
        int id= postRepository.findByTitle("title1").getId();
        logger.info(""+id);

        mockMvc.perform(delete(url+"/"+id));

        assertNull(postRepository.findById(id).get());
    }

    @Test
    void modify() {
    }

    public JoinRequestDto joinProc() {
        JoinRequestDto joinRequestDto = new JoinRequestDto();
        joinRequestDto.setEmail("aaa@aaa.ccc");
        joinRequestDto.setPassword("123456");
        joinRequestDto.setName("박이름");
        joinRequestDto.setPhone("010-3333-2222");
        joinRequestDto.setAge(20);
        joinRequestDto.setGender(Gender.MALE);
        joinRequestDto.setRole(Role.ROLE_USER);

        return joinRequestDto;
    }
}