package com.parksupark.soomjae.server.auth.service;

import com.parksupark.soomjae.server.auth.common.exception.RefreshFailedException;
import com.parksupark.soomjae.server.auth.jwt.JwtProvider;
import com.parksupark.soomjae.server.auth.jwt.service.RefreshTokenService;
import com.parksupark.soomjae.server.auth.username.dto.UsernamePasswordAuthSuccessResponse;
import com.parksupark.soomjae.server.common.exception.ErrorMessages;
import com.parksupark.soomjae.server.member.dto.MemberResponse;
import com.parksupark.soomjae.server.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DefaultAuthService implements AuthService{

    private final RefreshTokenService refreshTokenService;
    private final MemberService memberService;
    private final JwtProvider jwtProvider;

    @Override
    @Transactional(readOnly = true)
    public UsernamePasswordAuthSuccessResponse refresh(String refreshToken) {

        if (!refreshTokenService.validateRefreshToken(refreshToken)) {
            throw new RefreshFailedException(ErrorMessages.REFRESH_TOKEN_VALIDATION_FAILED_MESSAGE);
        }

        Long memberId = refreshTokenService.readMemberIdFromToken(refreshToken);

        MemberResponse memberResponse = memberService.readMember(memberId);
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", memberResponse.getRole().getKey());

        String newAccessToken = jwtProvider.generateAccessToken(memberResponse.getEmail(), claims);

        return new UsernamePasswordAuthSuccessResponse(newAccessToken, memberId);
    }
}
