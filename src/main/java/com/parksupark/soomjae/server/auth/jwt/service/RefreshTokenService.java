package com.parksupark.soomjae.server.auth.jwt.service;

import com.parksupark.soomjae.server.auth.jwt.dto.CreateRefreshTokenRequest;
import com.parksupark.soomjae.server.auth.jwt.entity.RefreshToken;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(CreateRefreshTokenRequest request);

    boolean validateRefreshToken(String token);

    Long readMemberIdFromToken(String token);
}
