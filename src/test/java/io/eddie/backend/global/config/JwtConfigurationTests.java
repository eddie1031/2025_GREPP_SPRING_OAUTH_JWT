package io.eddie.backend.global.config;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class JwtConfigurationTests {

    @Autowired
    JwtConfiguration jwtConfiguration;

    @Test
    @DisplayName("jwt configuration에 진짜 값이 있을까?")
    void test_1() throws Exception {

        String appKey = jwtConfiguration.getSecrets().getAppKey();
        log.info("appKey = {}", appKey);


    }



}