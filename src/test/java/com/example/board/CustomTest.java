package com.example.board;

import com.example.board.config.auth.PrincipalDetails;
import com.example.board.model.post.Kind;
import com.example.board.model.post.Post;
import com.example.board.model.post.PostDto;
import com.example.board.model.user.User;
import com.example.board.repository.PostRepository;
import com.example.board.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CustomTest extends BaseTest {

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

        postRepository.deleteAll();
    }

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @Test
    @WithMockUser
    public void 글을_삭제했을때_유저의_글_리스트에는_글이_남아있을까에_대한_테스트() throws Exception {
        //given
        for (int i = 1; i <= 5; i++) {
            postProc(i);
        }
        //when
        mockMvc.perform(delete("https://localhost:8080/posts/" + 3));

        //then
        MvcResult result = mockMvc.perform(get("https://localhost:8080/posts/users/" + 1)).andReturn();

        assertThat(result.getResponse().getContentAsString()).doesNotContain("post_3");
        assertThat(postRepository.findAll().size()).isEqualTo(4);
        // 결론 : 리스트 내부에 존재하는 글을 따로 지워줘야됨, 특정 엔티티와 연관이 있으면 글을 지워도 아예 안지워지는 경우도 생김
    }

    @Test
    @WithMockUser
    public void 글을_수정했을때_유저의_글_리스트에는_글이_수정될까에_대한_테스트() throws Exception {
        //given

        for (int i = 1; i <= 5; i++) {
            postProc(i);
        }

        PostDto postDto = PostDto.builder().
                title("title2").
                content("content2").
                userEmail(email).
                kind(Kind.NORMAL).
                build();

        //when
        mockMvc.perform(put("https://localhost:8080/posts/" + 3)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(postDto)))
                .andExpect(status().isOk());

        //then
        MvcResult result = mockMvc.perform(get("https://localhost:8080/board/normal?page=&size=10&sort=&keyword=")).andReturn();

        String[] r_str = result.getResponse().getContentAsString().split("},");

        assertThat(r_str[2]).contains("title2");
        // 결론 : 따로 유저의 리스트에서 작업하지 않아도 알아서 바뀐채로 적용됨
    }

    @Test
    @WithMockUser
    public void 회원탈퇴시_유저의_글은_어떻게_되는가에_대한_테스트() throws Exception {
        //given
        for (int i = 1; i <= 5; i++) {
            postProc(i);
        }

        //when
        mockMvc.perform(delete("https://localhost:8080/users/" + 1));

        //then
        assertThat(postRepository.findAll().size()).isEqualTo(0);
        // 결론 : 글 알아서 지워짐
    }

    public void postProc(int postIndex) throws Exception {
        PostDto postDto = PostDto.builder()
                .title("post_" + postIndex)
                .kind(Kind.NORMAL)
                .build();

        mockMvc.perform(post("https://localhost:8080/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(postDto)));
    }
}



