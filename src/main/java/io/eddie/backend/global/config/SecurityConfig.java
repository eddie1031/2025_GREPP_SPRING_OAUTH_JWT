package io.eddie.backend.global.config;

import io.eddie.backend.member.app.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final MemberService memberService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {
        return http
                .httpBasic(httpB -> httpB.disable())
                .csrf(csrf -> csrf.disable())
                .cors( cors -> cors.disable())
                .formLogin(form -> form.disable() )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .oauth2Login(Customizer.withDefaults())
                .oauth2Login( oauth -> {
                    oauth.successHandler(oAuth2SuccessHandler);
                })
                .authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                                .requestMatchers("/user/**")
                                    .hasAnyAuthority("ADMIN", "MEMBER")
                                .requestMatchers("/admin/**")
                                    .hasAnyAuthority("ADMIN")
                                .anyRequest()
                                    .authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
