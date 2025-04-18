package io.eddie.backend.member.domain;

import io.eddie.backend.member.dto.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role = Role.MEMBER;

    private String provider;

    private LocalDateTime signedAt = LocalDateTime.now();

    @Builder
    public Member(String username, String provider, String email) {
        this.username = username;
        this.provider = provider;
        this.email = email;
    }

}


