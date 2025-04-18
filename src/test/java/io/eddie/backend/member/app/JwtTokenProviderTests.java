package io.eddie.backend.member.app;

import io.eddie.backend.member.dto.Role;
import io.eddie.backend.member.dto.TokenBody;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
class JwtTokenProviderTests {

    @Autowired
    JwtTokenProvider provider;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("JWT 발급 테스트")
    void jwt_issue_test() throws Exception {

        Long targetId = 1L;
        Role targetRole = Role.MEMBER;

        String token = provider.issueAccessToken(targetId, targetRole);

        log.info("token = {}", token);
        assertThat(token).isNotNull();
        assertThat(token).isNotBlank();

        assertThat(token.split("\\.").length).isEqualTo(3);

    }

    @Test
    @DisplayName("유효성 검사 테스트")
    void validate_test() throws Exception {

        String targetToken1 = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZSI6Ik1FTUJFUiIsImlhdCI6MTc0NDk0Njc1OCwiZXhwIjoxNzQ0OTQ2ODE4fQ.NOpu5AaMnp-hWQnWImUyjMABCifmsLHAPzbPt3GlZ0w";
        String targetToken2 = "ㅋㅋ..ㅎㅎ..ㅈㅅ;";
        String targetToken3 = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

        boolean validate1 = provider.validate(targetToken1);
        boolean validate2 = provider.validate(targetToken2);
        boolean validate3 = provider.validate(targetToken3);

        assertThat(validate1).isFalse();
        assertThat(validate2).isFalse();
        assertThat(validate3).isFalse();

    }

    @Test
    @DisplayName("parse JWT Test")
    void parse_jwt_test() throws Exception {

        // given
        Long targetId = 100L;
        Role targetRole = Role.MEMBER;

        String targetToken = jwtTokenProvider.issueAccessToken(targetId, targetRole);

        boolean result = jwtTokenProvider.validate(targetToken);
        assertThat(result).isTrue();

        // when
        TokenBody tokenBody = jwtTokenProvider.parseJwt(targetToken);

        // then
        assertThat(tokenBody.getMemberId()).isEqualTo(targetId);
        assertThat(tokenBody.getRole()).isEqualTo(targetRole);

    }



}