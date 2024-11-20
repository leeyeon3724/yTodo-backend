package com.yeon.ytodo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")  // "test" 프로파일 활성화
class YTodoApplicationTests {

	@Test
	void contextLoads() {
		// 테스트 실행
	}
}
