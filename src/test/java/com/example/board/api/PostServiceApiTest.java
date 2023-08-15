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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

        postIndex = 0;
    }

    @Test
    @WithMockUser
    public void defaultSearchTest() throws Exception {
        //given
        List<Integer> postIdList = new ArrayList<>();

        for (int i = 1; i <= 30; i++) {
            postIdList.add(postProc());
        }

        //when
        MvcResult result = mockMvc.perform(get("https://localhost:8080/board/normal?page=&size=&sort=&keyword=")).andReturn();

        //then
        String r_str = result.getResponse().getContentAsString();
        String[] strings = r_str.split("},");

        assertThat(strings.length).isEqualTo(3); // default size = 3
        assertThat(strings[0]).contains("post_29"); // 가장 마지막에 쓴 글, default sort = recent
        assertThat(r_str).doesNotContain("post_1"); // 페이징에 맞지 않는 글
    }

    @Test
    @WithMockUser
    public void pagingSearchTest() throws Exception {
        //given
        int page = 2;
        int size = 5;

        List<Integer> postIdList = new ArrayList<>();

        for (int i = 1; i <= 30; i++) {
            postIdList.add(postProc());
        }

        //when
        MvcResult result = mockMvc.perform(get("https://localhost:8080/board/normal?page=" + page + "&size=" + size + "&sort=&keyword=")).andReturn();

        //then
        String r_str = result.getResponse().getContentAsString();
        String[] strings = r_str.split("},");

        //2페이지 내용 -> post_19 ~ post_15

        assertThat(strings.length).isEqualTo(5); // size = 5
        assertThat(strings[0]).contains("post_19");
        assertThat(r_str).doesNotContain("post_20", "post_14"); // 페이징에 맞지 않는 글
    }

    @Test
    @WithMockUser
    public void sortByLikeSearchTest() throws Exception {
        //given
        List<Integer> postIdList = new ArrayList<>();

        for (int i = 1; i <= 30; i++) {
            postIdList.add(postProc());
        }

        likePost(postIdList.get(7));
        likePost(postIdList.get(18));
        likePost(postIdList.get(29));

        //when
        MvcResult resultTopLikes = mockMvc.perform(get("https://localhost:8080/board/normal?page=&size=&sort=likes&keyword=")).andReturn();

        //then
        //좋아요 순 top3
        String r_strLike = resultTopLikes.getResponse().getContentAsString();
        String[] stringsLike = r_strLike.split("},");

        assertThat(stringsLike.length).isEqualTo(3); // size = 3
        assertThat(r_strLike).contains("post_7", "post_18", "post_29");
    }

    @Test
    @WithMockUser
    public void sortByViewSearchTest() throws Exception {
        //given
        List<Integer> postIdList = new ArrayList<>();

        for (int i = 1; i <= 30; i++) {
            postIdList.add(postProc());
        }

        viewPost(postIdList.get(7));
        viewPost(postIdList.get(18));
        viewPost(postIdList.get(29));

        //when
        MvcResult resultTopViews = mockMvc.perform(get("https://localhost:8080/board/normal?page=&size=&sort=views&keyword=")).andReturn();

        //then
        //조회수 순 top3
        String r_strView = resultTopViews.getResponse().getContentAsString();
        String[] stringsView = r_strView.split("},");

        assertThat(stringsView.length).isEqualTo(3); // size = 3
        assertThat(r_strView).contains("post_7", "post_18", "post_29");
    }

    @Test
    @WithMockUser
    public void keywordSearchTest() throws Exception {
        //given
        List<Integer> postIdList = new ArrayList<>();

        for (int i = 1; i <= 30; i++) {
            postIdList.add(postProc());
        }

        //when
        MvcResult result = mockMvc.perform(get("https://localhost:8080/board/normal?page=&size=20&sort=&keyword=post_3")).andReturn();

        //then
        String r_str = result.getResponse().getContentAsString();
        String[] strings = r_str.split("},");

        assertThat(strings.length).isEqualTo(1); // size = 20이지만 합당한 검색결과는 post_3 단 1개
        assertThat(r_str).contains("post_3");
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

    public Integer postProc() throws Exception {
        // 새로운 글 쓰기 + 글 등록 + 해당 글 id 반환
        PostDto postDto = PostDto.builder()
                .title("post_" + postIndex)
                .kind(Kind.NORMAL)
                .build();

        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(postDto)));

        Integer id = postRepository.findByTitle("post_" + postIndex).getId();

        postIndex++;

        return id;
    }

    public void viewPost(Integer postIdx) throws Exception {
        mockMvc.perform(get("https://localhost:8080/posts/" + postIdx));
    }

    public void likePost(Integer postIdx) throws Exception {
        mockMvc.perform(put(url + "/" + postIdx + "/like?value=true"));
    }
}
