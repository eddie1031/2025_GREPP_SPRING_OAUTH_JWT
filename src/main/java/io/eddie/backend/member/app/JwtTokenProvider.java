package io.eddie.backend.member.app;

import io.eddie.backend.global.config.JwtConfiguration;
import io.eddie.backend.member.dao.RefreshTokenBlackListRepository;
import io.eddie.backend.member.dao.RefreshTokenRepository;
import io.eddie.backend.member.dao.TokenRepository;
import io.eddie.backend.member.domain.Member;
import io.eddie.backend.member.domain.RefreshToken;
import io.eddie.backend.member.dto.Role;
import io.eddie.backend.member.dto.TokenBody;
import io.eddie.backend.member.dto.TokenPair;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtConfiguration jwtConfiguration;
//    private final RefreshTokenRepository refreshTokenRepository;
//    private final RefreshTokenBlackListRepository refreshTokenBlackListRepository;

    private final TokenRepository tokenRepository;


    public TokenPair generateTokenPair(Member member) {

        String accessToken = issueAccessToken(member.getId(), member.getRole());
        String refreshToken = issueRefreshToken(member.getId(), member.getRole());

        tokenRepository.save(member, refreshToken);

        return TokenPair.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public Optional<RefreshToken> findRefreshToken(Long memberId) {
        return tokenRepository.findValidRefToken(memberId);
    }

    public String issueAccessToken(Long id, Role role) {
        return issue(id, role, jwtConfiguration.getValidation().getAccess());
    }

    public String issueRefreshToken(Long id, Role role) {
        return issue(id, role, jwtConfiguration.getValidation().getRefresh());
    }

    private String issue(Long id, Role role, Long expTime) {
        return Jwts.builder()
                .subject(id.toString())
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + expTime))
                .signWith(getSecretKey(), Jwts.SIG.HS256)
                .compact();
    }

    public boolean validate(String token) {

        try {
            Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token);

            return true;
        } catch ( JwtException e ) {
            log.error("token = {}", token);
            log.error("토큰이 이상해요..");
        } catch ( IllegalStateException e ) {
            log.error("token = {}", token);
            log.error("이상한 토큰이 검출되었습니다.");
        } catch (Exception e) {
            log.error("token = {}", token);
            log.error(";;");
        }

        return false;

    }

    public TokenBody parseJwt(String token) {

        Jws<Claims> parsed = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token);

        String sub = parsed.getPayload().getSubject();
        String role = parsed.getPayload().get("role").toString();

        return new TokenBody(
                Long.parseLong(sub)
                , Role.valueOf(role)
        );

    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtConfiguration.getSecrets().getAppKey().getBytes());
    }

}
