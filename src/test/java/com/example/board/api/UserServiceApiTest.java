package com.example.board.api;

import com.example.board.BaseTest;
import com.example.board.model.user.Gender;
import com.example.board.model.user.Role;
import com.example.board.model.user.User;
import com.example.board.model.user.userDto.JoinRequestDto;
import com.example.board.model.user.userDto.UpdateRequestDto;
import com.example.board.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class UserServiceApiTest extends BaseTest {

    @Autowired
    public UserRepository userRepository;

    String url = "https://localhost:8080/users";

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        userRepository.deleteAll();
    }

    @Test
    @WithAnonymousUser
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
        assertThat(user.getRole()).isEqualTo(Role.ROLE_USER);
    }

    @Test
    @WithMockUser(roles = "USER")
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
    @WithMockUser(roles = "USER")
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
}
