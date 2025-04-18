package io.eddie.backend.member.app;

import io.eddie.backend.member.dto.MemberDetails;
import io.eddie.backend.member.exceptions.UnavailableProviderException;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

@SuppressWarnings("all")
public class MemberDetailsFactory {

    public static MemberDetails memberDetails(String provider, OAuth2User oAuth2User) {

        Map<String, Object> attributes = oAuth2User.getAttributes();

        switch ( provider.toUpperCase().trim() ) {

            case "GOOGLE" -> {
                return MemberDetails.builder()
                        .name(attributes.get("name").toString())
                        .email(attributes.get("email").toString())
                        .attributes(attributes)
                        .build();
            }

            case "NAVER" -> {
                Map<String, String> properties = (Map<String, String>) attributes.get("response");
                return MemberDetails.builder()
                        .name(properties.get("name"))
                        .email(properties.get("id") + "@naver.com")
                        .attributes(attributes)
                        .build();
            }

            case "KAKAO" -> {
                Map<String, String> properties = (Map<String, String>) attributes.get("properties");
                return MemberDetails.builder()
                        .name(properties.get("nickname"))
                        .email(attributes.get("id").toString() + "@kakao.com")
                        .build();
            }

            default -> throw new UnavailableProviderException("지원하지 않는 제공자 : " + provider);

        }

    }

}
