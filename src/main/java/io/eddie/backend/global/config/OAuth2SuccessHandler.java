package io.eddie.backend.global.config;


import io.eddie.backend.member.app.JwtTokenProvider;
import io.eddie.backend.member.app.MemberService;
import io.eddie.backend.member.domain.Member;
import io.eddie.backend.member.domain.RefreshToken;
import io.eddie.backend.member.dto.MemberDetails;
import io.eddie.backend.member.dto.TokenPair;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        MemberDetails principal = (MemberDetails) authentication.getPrincipal();

        Member findMember = memberService.getById(principal.getId());

        HashMap<String, String> params = new HashMap<>();

        Optional<RefreshToken> refreshTokenOptional = jwtTokenProvider.findRefreshToken(principal.getId());

        if ( refreshTokenOptional.isEmpty() ) {
            TokenPair tokenPair = jwtTokenProvider.generateTokenPair(findMember);
            params.put("access", tokenPair.getAccessToken());
            params.put("refresh", tokenPair.getRefreshToken());
        } else {
            String accessToken = jwtTokenProvider.issueAccessToken(principal.getId(), principal.getRole());
            params.put("access", accessToken);
            params.put("refresh", refreshTokenOptional.get().getRefreshToken());
        }

        String access = params.get("access");
        String refresh = params.get("refresh");

        log.info("access = {}", access);
        log.info("refresh = {}", refresh);

    }

}
