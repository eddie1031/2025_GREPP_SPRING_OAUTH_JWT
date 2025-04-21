package io.eddie.backend.member.dao;

import io.eddie.backend.member.domain.Member;
import io.eddie.backend.member.domain.RefreshToken;
import io.eddie.backend.member.domain.RefreshTokenBlackList;

import java.util.Optional;

public interface TokenRepository {

    RefreshToken save(Member member, String token);
    RefreshTokenBlackList addBlackList(RefreshToken refreshToken);
    Optional<RefreshToken> findValidRefToken(Long memberId);

}
