package com.example.board.api;

import com.example.board.BaseTest;
import com.example.board.user.Gender;
import com.example.board.user.Role;
import com.example.board.user.User;
import com.example.board.user.UserDto.JoinRequestDto;
import com.example.board.user.UserDto.LoginRequestDto;
import com.example.board.user.UserDto.SessionUserDto;
import com.example.board.user.UserDto.UpdateRequestDto;
import com.example.board.user.except.IncorrectPasswordException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class UserServiceApiTest extends BaseTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MockHttpSession mockHttpSession;
    @Autowired
    WebApplicationContext context;

    String url = "https://localhost:8080/users";

    String email = "email@temp.net";
    String password = "password";

    @BeforeEach
    public void clearDB() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        userRepository.deleteAll();
    }

    @Test
    //@WithMockUser(roles = "USER")
    public void joinTest() throws Exception {
        //given
        JoinRequestDto joinRequestDto = joinProc();

        //when
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(joinRequestDto)))
                .andExpect(status().isOk());

        //then
        User user = userRepository.findByEmail(email);

        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getRole()).isEqualTo(Role.USER);
    }

    @Test
    public void successLoginTest() throws Exception {
        //given
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(joinProc())))
                .andExpect(status().isOk());

        LocalDateTime joinDate = userRepository.findByEmail(email).getRecentLoginDate();

        //when
        mockMvc.perform(post(url + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginProc(email, password))))
                .andExpect(status().isOk());

        //then
        User user = userRepository.findByEmail(email);

        assertThat(joinDate).isBefore(user.getRecentLoginDate());
    }

    @Test
    public void wrongPasswordLoginTest() throws Exception {
        //given
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(joinProc())))
                .andExpect(status().isOk());

        //when
        try {
            mockMvc.perform(post(url + "/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(loginProc(email, password + "wrong"))))
                    .andExpect(status().isOk()).andDo(result -> System.out.println(result));
        } catch (IncorrectPasswordException e) {
            return;
        }
        fail("");
    }

    @Test
    public void sessionTest() throws Exception {
        //given
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(joinProc())))
                .andExpect(status().isOk());


        //when
        mockMvc.perform(post(url + "/login").session(mockHttpSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginProc(email, password))))
                .andExpect(status().isOk());

        //then
        assertThat(mockHttpSession.getAttribute("user")).isInstanceOf(SessionUserDto.class);

        SessionUserDto dto = (SessionUserDto) mockHttpSession.getAttribute("user");

        User user = userRepository.findByEmail(dto.getEmail());

        assertThat(user.getEmail()).isEqualTo(email);
    }

    // 실패작, 시간이 지나도 세션 만료가 안됨
    @Test
    public void sessionTimeOutTest() throws Exception {
        //given
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(joinProc())))
                .andExpect(status().isOk());

        //when
        HttpSession httpSession = mockMvc.perform(post(url + "/login").session(mockHttpSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginProc(email, password))))
                .andExpect(status().isOk()).andReturn().getRequest().getSession();

        //then
        assertThat(mockHttpSession.getAttribute("user")).isNotNull();
        System.out.println(httpSession.getAttribute("user"));
        System.out.println(mockHttpSession.getAttribute("user"));

        Thread.sleep(2 * 1000);

        System.out.println(mockHttpSession.getAttribute("user"));
        System.out.println(httpSession.getAttribute("user"));
        System.out.println("테스트 : " + mockHttpSession);
    }

    @Test
    public void updateTest() throws Exception {
        //given
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(joinProc())))
                .andExpect(status().isOk());

        UpdateRequestDto updateRequestDto = new UpdateRequestDto();

        updateRequestDto.setPassword("새로운 비밀번호");
        updateRequestDto.setGender(Gender.FEMALE);
        updateRequestDto.setName("새로운 이름");

        Integer id = userRepository.findByEmail(email).getId();

        //when
        mockMvc.perform(put(url + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateRequestDto)))
                .andExpect(status().isOk());

        //then
        User user = userRepository.findByEmail(email);

        assertThat(user.getName()).isEqualTo("새로운 이름");
        assertThat(user.getPassword()).isEqualTo("새로운 비밀번호");
        assertThat(user.getGender()).isEqualTo(Gender.FEMALE);
    }

    @Test
    public void deleteTest() throws Exception {
        //given
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(joinProc())))
                .andExpect(status().isOk());

        Integer id = userRepository.findByEmail(email).getId();

        //when
        mockMvc.perform(delete(url + "/" + id));

        //then
        User user = userRepository.findByEmail(email);
        List<User> userList = userRepository.findAll();

        assertThat(user).isEqualTo(null);
        assertThat(userList.size()).isEqualTo(0);
    }

    public JoinRequestDto joinProc() {
        JoinRequestDto joinRequestDto = new JoinRequestDto();
        joinRequestDto.setEmail(email);
        joinRequestDto.setPassword(password);
        joinRequestDto.setName("박이름");
        joinRequestDto.setCall("010-3333-2222");
        joinRequestDto.setAge(20);
        joinRequestDto.setGender(Gender.MALE);

        return joinRequestDto;
    }

    public LoginRequestDto loginProc(String email, String password) {
        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setEmail(email);
        loginRequestDto.setPassword(password);

        return loginRequestDto;
    }

}
