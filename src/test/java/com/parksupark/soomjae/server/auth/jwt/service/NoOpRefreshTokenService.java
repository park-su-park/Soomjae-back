package com.parksupark.soomjae.server.auth.jwt.service;

import com.parksupark.soomjae.server.auth.jwt.dto.CreateRefreshTokenRequest;
import com.parksupark.soomjae.server.auth.jwt.entity.RefreshToken;

public class NoOpRefreshTokenService implements RefreshTokenService {
    @Override
    public Long readMemberIdFromToken(String token) {
        return 0L;
    }

    @Override
    public boolean validateRefreshToken(String token) {
        return false;
    }

    @Override
    public RefreshToken createRefreshToken(CreateRefreshTokenRequest request) {
        return null;
    }
}
