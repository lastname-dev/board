package com.example.board;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;
import org.springframework.util.AntPathMatcher;


@SpringBootTest
class BoardApplicationTests {

	@Test
	void contextLoads() {

		AntPathMatcher pathMatcher = new AntPathMatcher();

		assertThat(pathMatcher.match("**/posts?kind=MANAGE","https://localhost:8080/posts?kind=MANAGE")).isEqualTo(true);
	}

}
