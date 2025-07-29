package com.parksupark.soomjae.server.auth.jwt.service;

import com.parksupark.soomjae.server.auth.jwt.JwtProvider;
import com.parksupark.soomjae.server.auth.jwt.dto.CreateRefreshTokenRequest;
import com.parksupark.soomjae.server.auth.jwt.entity.RefreshToken;
import com.parksupark.soomjae.server.auth.jwt.repository.RefreshTokenRepository;
import com.parksupark.soomjae.server.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class DefaultRefreshTokenService implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProvider jwtProvider;

    private final String refreshTokenNotFoundMessage = "refresh token 조회 실패";

    @Transactional
    public RefreshToken createRefreshToken(CreateRefreshTokenRequest request) {
        String token = jwtProvider.generateRefreshToken(request.getSubject());
        Instant expiresAt = jwtProvider.getClaimsFromRefreshToken(token).getExpiration()
            .toInstant();
        
        // 기존 토큰 삭제
        refreshTokenRepository.deleteByMemberId(request.getMemberId());
        refreshTokenRepository.flush();

        RefreshToken refreshToken = RefreshToken.create(token, request.getMemberId(), expiresAt);
        refreshTokenRepository.save(refreshToken);

        return refreshToken;
    }

    @Transactional(readOnly = true)
    public boolean validateRefreshToken(String token) {
        return jwtProvider.validateRefreshToken(token) &&
                refreshTokenRepository.existsByToken(token);
    }

    @Transactional
    public Long readMemberIdFromToken(String token) {
        return refreshTokenRepository.findByToken(token)
            .map(RefreshToken::getMemberId)
            .orElseThrow(() -> new ResourceNotFoundException(refreshTokenNotFoundMessage));
    }

}
