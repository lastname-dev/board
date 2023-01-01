package com.example.board.controller;

import com.example.board.BaseTest;
import com.example.board.config.auth.PrincipalDetails;
import com.example.board.model.comment.Comment;
import com.example.board.model.comment.CommentDto;
import com.example.board.model.post.Kind;
import com.example.board.model.post.Post;
import com.example.board.model.post.PostDto;
import com.example.board.model.user.Gender;
import com.example.board.model.user.Role;
import com.example.board.model.user.User;
import com.example.board.model.user.userDto.JoinRequestDto;
import com.example.board.repository.CommentRepository;
import com.example.board.repository.PostRepository;
import com.example.board.repository.UserRepository;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.reactive.server.WebTestClient;


import javax.transaction.Transactional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



class PostControllerTest extends BaseTest {

    @Autowired
    private ObjectMapper objectMapper;

    String url = "https://localhost:8080/posts";

    @Autowired
    UserRepository userRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    CommentRepository commentRepository;

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

    @Test
    void viewBoard() {

    }

    @Test
    void read() throws Exception {
        write();
        Integer id = postRepository.findByTitle("title1").getId();
        mockMvc.perform(get(url + "/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[?(@.title == 'title1')]").exists());

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
//    @Order(200)
    void deleteTest() throws Exception {
        write();


        int id = postRepository.findByTitle("title1").getId();

        mockMvc.perform(delete(url + "/" + id));

        assertThat(postRepository.findById(id)).isEmpty();
        assertThat(postRepository.findAll().size()).isEqualTo(0);
    }

    @Test
//    @Order(150)
    void modify() throws Exception {
        write();
        Post post = postRepository.findByTitle("title1");
        int id = post.getId();
        String userEmail = post.getUser().getEmail();

        PostDto input = PostDto.builder().
                title("title2").
                content("content2").
                userEmail(userEmail).
                build();

        mockMvc.perform(put(url + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk());

        assertThat(postRepository.findById(id).get().getTitle()).isEqualTo("title2");
        assertThat(postRepository.findAll().size()).isEqualTo(1);
    }
}