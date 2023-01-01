package com.example.board;

import com.example.board.model.user.Gender;
import com.example.board.model.user.Role;
import com.example.board.model.user.userDto.JoinRequestDto;
import com.example.board.repository.UserRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;

@Transactional
@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@WebAppConfiguration
public class BaseTest {
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected WebApplicationContext context;

    protected static final String email = "email@temp.com";
    protected static final String password = "1234";

    public JoinRequestDto joinProc() {
        JoinRequestDto joinRequestDto = new JoinRequestDto();
        joinRequestDto.setEmail(email);
        joinRequestDto.setPassword(password);
        joinRequestDto.setName("박이름");
        joinRequestDto.setPhone("010-3333-2222");
        joinRequestDto.setAge(20);
        joinRequestDto.setGender(Gender.MALE);
        joinRequestDto.setRole(Role.ROLE_USER);

        return joinRequestDto;
    }


}
