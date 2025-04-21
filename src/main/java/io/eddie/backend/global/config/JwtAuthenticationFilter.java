package io.eddie.backend.global.config;

import io.eddie.backend.member.app.JwtTokenProvider;
import io.eddie.backend.member.app.MemberService;
import io.eddie.backend.member.dto.MemberDetails;
import io.eddie.backend.member.dto.TokenBody;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Enumeration;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = resolveToken(request);

        if ( token != null && jwtTokenProvider.validate(token) ) {

            TokenBody tokenBody = jwtTokenProvider.parseJwt(token);
            MemberDetails memberDetails = memberService.getMemberDetailsById(tokenBody.getMemberId());

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    memberDetails, token, memberDetails.getAuthorities()
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

        }

        filterChain.doFilter(request, response);

    }

    private String resolveToken(HttpServletRequest request) {

        String bearerToken = request.getHeader("Authorization");

        if ( bearerToken != null && bearerToken.startsWith("Bearer ") ) {
            return bearerToken.substring(7);
        }

        return null;

    }


}
