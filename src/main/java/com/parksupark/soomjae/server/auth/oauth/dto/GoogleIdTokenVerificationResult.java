package com.parksupark.soomjae.server.auth.oauth.dto;

import com.parksupark.soomjae.server.auth.jwt.entity.RefreshToken;
import com.parksupark.soomjae.server.member.entity.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class GoogleIdTokenVerificationResult {

    private final String accessToken;
    private final RefreshToken refreshToken;
    private final Member member;

    public static GoogleIdTokenVerificationResult success(String accessToken,
        RefreshToken refreshToken, Member member) {
        return new GoogleIdTokenVerificationResult(accessToken, refreshToken, member);
    }

}
