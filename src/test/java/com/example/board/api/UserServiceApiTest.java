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
    private MockHttpSession httpSession;

    String url = "https://localhost:8080/users";

    String email = "email@temp.net";

    @BeforeEach
    public void clearDB() {
        userRepository.deleteAll();
        System.out.println("저장소 삭제");
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

        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setEmail(email);
        loginRequestDto.setPassword("비밀번호");

        //when
        mockMvc.perform(post(url + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginRequestDto)))
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

        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setEmail(email);
        loginRequestDto.setPassword("잘못된 비밀번호");

        //when
        try {
            mockMvc.perform(post(url + "/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(loginRequestDto)))
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

        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setEmail(email);
        loginRequestDto.setPassword("비밀번호");

        //when
        mockMvc.perform(post(url + "/login").session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginRequestDto)))
                .andExpect(status().isOk());

        //then
        assertThat(httpSession.getAttribute(email)).isInstanceOf(SessionUserDto.class);

        SessionUserDto dto = (SessionUserDto) httpSession.getAttribute(email);

        User user = userRepository.findByEmail(dto.getEmail());

        assertThat(user.getEmail()).isEqualTo(email);
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
        joinRequestDto.setEmail("email@temp.net");
        joinRequestDto.setPassword("비밀번호");
        joinRequestDto.setName("박이름");
        joinRequestDto.setCall("010-3333-2222");
        joinRequestDto.setAge(20);
        joinRequestDto.setGender(Gender.MALE);

        return joinRequestDto;
    }

}
