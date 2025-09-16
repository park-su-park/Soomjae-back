package com.parksupark.soomjae.server.auth.jwt.repository;

import com.parksupark.soomjae.server.auth.jwt.entity.RefreshToken;
import java.util.Optional;

public interface RefreshTokenRepository {

    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByMemberId(Long memberId);

    RefreshToken save(RefreshToken refreshToken);

    void deleteByToken(String token);

    void deleteByMemberId(Long memberId);

    boolean existsByToken(String token);

    void flush();

}
