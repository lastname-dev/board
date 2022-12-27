package com.example.board.api;

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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MvcResult;


import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class PostServiceApiTest extends BaseTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;

    String url = "https://localhost:8080/posts";

    @BeforeEach
    public void init() throws Exception {
        userRepository.deleteAll();
        postRepository.deleteAll();

        User user = new User(joinProc());
        PrincipalDetails userDetails = new PrincipalDetails(user);

        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities()));
    }

    @Test
    public void searchTest() throws Exception {

        assertThat(userRepository.findAll().get(0).getEmail()).isEqualTo("email");

        for (int i = 0; i < 5; i++) {
            PostDto postDto = PostDto.builder()
                    .title("글번호" + i)
                    .kind(Kind.NORMAL)
                    .build();

            mockMvc.perform(post(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(postDto)));
        }

        //when
        MvcResult result = mockMvc.perform(get(url + "/board/{kind}" + "?page=0&size=3&sort=recent&keyword="))
                .andExpect(status().isOk())
                .andReturn();

        //then
        MockHttpServletResponse response = result.getResponse();

        System.out.println(response);
    }

    public JoinRequestDto joinProc() throws Exception{
        JoinRequestDto joinRequestDto = new JoinRequestDto();
        joinRequestDto.setEmail("email");
        joinRequestDto.setPassword("password");
        joinRequestDto.setName("박이름");
        joinRequestDto.setPhone("010-3333-2222");
        joinRequestDto.setAge(20);
        joinRequestDto.setGender(Gender.MALE);
        joinRequestDto.setRole(Role.ROLE_USER);

        mockMvc.perform(post("https://localhost:8080/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(joinRequestDto)))
                .andExpect(status().isOk());

        return joinRequestDto;
    }

}
