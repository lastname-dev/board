package com.example.board.api;

import com.example.board.BaseTest;
import com.example.board.model.post.PostDto;
import com.example.board.repository.PostRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    @WithMockUser(roles = "USER")
    public void searchTest() throws Exception {
        //given
        for (int i = 0; i < 5; i++) {

            PostDto postDto = PostDto.builder()
                    .title("글번호" + i)
                    .build();

            mockMvc.perform(post(url)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(new ObjectMapper().writeValueAsString(postDto)));
        }

        //when
        ResultActions actions = mockMvc.perform(get(url + "/board/{kind}" + "?page=0&size=3&sort=&keyword="));

        //then
        actions.andExpect(status().isOk())
                .andExpect()
    }


}
