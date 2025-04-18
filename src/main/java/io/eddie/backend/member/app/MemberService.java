package io.eddie.backend.member.app;

import io.eddie.backend.member.dao.MemberRepository;
import io.eddie.backend.member.domain.Member;
import io.eddie.backend.member.dto.MemberDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("oAuth2User = {}", oAuth2User);

        String providerId = userRequest.getClientRegistration().getRegistrationId().toUpperCase();

        MemberDetails memberDetails = MemberDetailsFactory.memberDetails(providerId, oAuth2User);

        Optional<Member> memberOptional = memberRepository.findByEmail(memberDetails.getEmail());

        Member findMember = memberOptional.orElseGet(() -> {
            Member member = Member.builder()
                    .username(memberDetails.getName())
                    .email(memberDetails.getEmail())
                    .provider(providerId)
                    .build();
            return memberRepository.save(member);
        });

        if ( findMember.getProvider().equals(providerId) ) {
            return memberDetails.setId(findMember.getId()).setRole(findMember.getRole());
        } else {
            throw new IllegalStateException("이미 다른 이메일로 가입되어있는 유저입니다. 다시 로그인해주세요");
        }

    }


}
