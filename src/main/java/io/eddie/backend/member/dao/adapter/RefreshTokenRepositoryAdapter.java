package io.eddie.backend.member.dao.adapter;


import io.eddie.backend.member.dao.RefreshTokenBlackListRepository;
import io.eddie.backend.member.dao.RefreshTokenRepository;
import io.eddie.backend.member.dao.TokenRepository;
import io.eddie.backend.member.domain.Member;
import io.eddie.backend.member.domain.RefreshToken;
import io.eddie.backend.member.domain.RefreshTokenBlackList;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepositoryAdapter implements TokenRepository {

    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenBlackListRepository refreshTokenBlackListRepository;

    private final EntityManager entityManager;

    @Override
    public RefreshToken save(Member member, String token) {
        return refreshTokenRepository.save(
            RefreshToken.builder()
                    .member(member)
                    .refreshToken(token)
                    .build()
        );
    }

    @Override
    public RefreshTokenBlackList addBlackList(RefreshToken refreshToken) {
        return refreshTokenBlackListRepository.save(
            RefreshTokenBlackList.builder()
                    .refreshToken(refreshToken)
                    .build()
        );
    }

    @Override
    public Optional<RefreshToken> findValidRefToken(Long memberId) {
        return entityManager.createQuery(
                "select rf from RefreshToken rf left join RefreshTokenBlackList rtb on rtb.refreshToken = rf where rf.member.id = :memberId and rtb.id is null"
                        , RefreshToken.class)
                .setParameter("memberId", memberId)
                .getResultStream()
                .findFirst();
    }

}
