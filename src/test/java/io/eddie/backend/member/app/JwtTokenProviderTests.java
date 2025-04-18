package io.eddie.backend.member.app;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class JwtTokenProviderTests {

    @Autowired
    JwtTokenProvider provider;

//    @BeforeEach
//    void init() {
//        provider = new JwtTokenProvider();
//    }

    @Test
    @DisplayName("JWT 발급 테스트")
    void jwt_issue_test() throws Exception {

        String token = provider.issue();
        log.info("token = {}", token);

    }


}