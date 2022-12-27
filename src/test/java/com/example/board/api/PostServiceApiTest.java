package com.example.board.api;

import com.example.board.BaseTest;
import com.example.board.model.post.Kind;
import com.example.board.model.post.PostDto;
import com.example.board.model.user.Gender;
import com.example.board.model.user.Role;
import com.example.board.model.user.userDto.JoinRequestDto;
import com.example.board.repository.PostRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class PostServiceApiTest extends BaseTest {

    @Autowired
    private PostRepository postRepository;

    String url = "https://localhost:8080/posts";

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        postRepository.deleteAll();
    }

    @Test
    @WithMockUser
    public void searchTest() throws Exception {

        //loginProc();

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

    public void loginProc() throws Exception {
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

        mockMvc.perform(post("https://localhost:8080/users/login")
                        .param("email","email")
                        .param("password","password"))
                .andDo(print())
                .andExpect(authenticated());

    }

}
