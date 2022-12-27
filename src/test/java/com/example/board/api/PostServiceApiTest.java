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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


public class PostServiceApiTest extends BaseTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;

    String url = "https://localhost:8080/posts";

    Integer postIndex;

    @BeforeEach
    public void init() throws Exception {

        postRepository.deleteAll();

        User user = new User(joinProc());
        PrincipalDetails userDetails = new PrincipalDetails(user);

        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities()));

        mockMvc.perform(post("https://localhost:8080/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(joinProc())));

        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        postIndex=0;
    }

    @Test
    public void searchTest() throws Exception {

        for (int i = 0; i < 5; i++) {
            PostDto postDto = PostDto.builder()
                    .title("post_" + i)
                    .kind(Kind.NORMAL)
                    .build();

            mockMvc.perform(post(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(postDto)));
        }

        //when
        MvcResult result = mockMvc.perform(get("https://localhost:8080/board/normal?page=0&size=3&sort=&keyword=")).andReturn();

        //then
        String r_str = result.getResponse().getContentAsString();
        System.out.println(r_str); // 일단 페이징을 통해 글을 최신순으로 출력하는건 성공, sort 관련해서 해야함

    }

    @Test
    public void likeTest() throws Exception {
        //given
        Integer id = postProc();

        //when
        mockMvc.perform(put(url + "/" + id + "/like?value=true"));
        mockMvc.perform(put(url + "/" + id + "/like?value=false"));
        mockMvc.perform(put(url + "/" + id + "/like?value=false"));

        //then
        Post post = postRepository.findById(id).get();

        assertThat(post.getLikes()).isEqualTo(1);
        assertThat(post.getUnlikes()).isEqualTo(2);
    }
    @Test
    public void viewTest() throws Exception {
        //given
        Integer id = postProc();

        //when
        int repeatIndex = 10;
        for (int i = 0; i < repeatIndex; i++) {
            mockMvc.perform(get(url + "/" + id));
        }

        //then
        Post post = postRepository.findById(id).get();

        assertThat(post.getViews()).isEqualTo(repeatIndex);
    }

    public JoinRequestDto joinProc() {
        JoinRequestDto joinRequestDto = new JoinRequestDto();
        joinRequestDto.setEmail("email");
        joinRequestDto.setPassword("password");
        joinRequestDto.setName("박이름");
        joinRequestDto.setPhone("010-3333-2222");
        joinRequestDto.setAge(20);
        joinRequestDto.setGender(Gender.MALE);
        joinRequestDto.setRole(Role.ROLE_USER);

        return joinRequestDto;
    }
    public Integer postProc() throws Exception{
        // 새로운 글 쓰기 + 글 등록 + 해당 글 id 반환
        PostDto postDto = PostDto.builder()
                .title("post_" + postIndex)
                .kind(Kind.NORMAL)
                .build();

        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(postDto)));

        Integer id  = postRepository.findByTitle("post_" + postIndex).getId();

        postIndex++;

        return id;
    }

}
