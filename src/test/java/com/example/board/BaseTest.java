package com.example.board;

import com.example.board.repository.UserRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
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

}
