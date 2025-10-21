package com.parksupark.soomjae.server.auth.oauth.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.parksupark.soomjae.server.auth.jwt.JwtProvider;
import com.parksupark.soomjae.server.auth.jwt.dto.CreateRefreshTokenRequest;
import com.parksupark.soomjae.server.auth.jwt.entity.RefreshToken;
import com.parksupark.soomjae.server.auth.jwt.service.RefreshTokenService;
import com.parksupark.soomjae.server.auth.oauth.AuthProvider;
import com.parksupark.soomjae.server.auth.oauth.dto.GoogleIdTokenVerificationResult;
import com.parksupark.soomjae.server.auth.oauth.exception.OAuth2AuthenticationProcessingException;
import com.parksupark.soomjae.server.auth.oauth.userinfo.GoogleOAuth2UserInfo;
import com.parksupark.soomjae.server.auth.oauth.userinfo.OAuth2UserInfo;
import com.parksupark.soomjae.server.common.exception.ErrorMessages;
import com.parksupark.soomjae.server.member.entity.Member;
import com.parksupark.soomjae.server.member.repository.MemberRepository;
import com.parksupark.soomjae.server.member.util.RandomNicknameCreator;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class GoogleIdTokenService {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    private GoogleIdTokenVerifier verifier;
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    public GoogleIdTokenService(MemberRepository memberRepository, JwtProvider jwtProvider,
        RefreshTokenService refreshTokenService) {
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
        this.refreshTokenService = refreshTokenService;
    }

    @Transactional
    public GoogleIdTokenVerificationResult verify(String idTokenString) {
        try {
            GoogleIdToken idToken = verifyToken(idTokenString);
            OAuth2UserInfo userInfo = extractUserInfo(idToken);

            Member member = findOrCreateMember(userInfo);

            String accessToken = createAccessToken(member);
            RefreshToken refreshToken = createRefreshToken(member);

            return GoogleIdTokenVerificationResult.success(accessToken, refreshToken, member);

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new OAuth2AuthenticationProcessingException(
                ErrorMessages.OAUTH2_GOOGLE_ID_TOKEN_VERIFICATION_FAILED_MESSAGE);
        }
    }

    private GoogleIdToken verifyToken(String idTokenString)
        throws GeneralSecurityException, IOException {

        if (verifier == null) {
            verifier = new GoogleIdTokenVerifier.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(googleClientId))
                .build();
        }

        // Google 공개키로 서명 검증
        GoogleIdToken idToken = verifier.verify(idTokenString);

        if (idToken != null) {
            validateToken(idToken);
            return idToken;
        } else {
            throw new IllegalArgumentException(ErrorMessages.INVALID_GOOGLE_ID_TOKEN_MESSAGE);
        }
    }

    private void validateToken(GoogleIdToken idToken) {
        GoogleIdToken.Payload payload = idToken.getPayload();

        if (!("accounts.google.com".equals(payload.getIssuer())
            || "https://accounts.google.com".equals(payload.getIssuer()))) {
            throw new IllegalArgumentException(ErrorMessages.INVALID_ISSUER_MESSAGE);
        }

        Boolean emailVerified = payload.getEmailVerified();
        if (emailVerified == null || !emailVerified) {
            throw new IllegalArgumentException(ErrorMessages.OAUTH2_EMAIL_NOT_VERIFIED_MESSAGE);
        }
    }

    private OAuth2UserInfo extractUserInfo(GoogleIdToken idToken) {
        return new GoogleOAuth2UserInfo(idToken.getPayload());
    }

    private Member findOrCreateMember(OAuth2UserInfo userInfo) {
        Optional<Member> existingMember = memberRepository.findByProviderAndProviderId(
            AuthProvider.GOOGLE, userInfo.getProviderId());

        if (existingMember.isPresent()) {
            Member member = existingMember.get();

            log.info("기존 OAuth2 사용자 로그인: {}, provider: {}", userInfo.getEmail(),
                AuthProvider.GOOGLE);

            // 이메일 주소가 변경되었을 수 있으니 업데이트
            if (!member.getEmail().equals(userInfo.getEmail())) {
                member.updateEmail(userInfo.getEmail());
                memberRepository.save(member);
                log.info("OAuth2 사용자 이메일 주소 업데이트: {} -> {}", member.getEmail(),
                    userInfo.getEmail());
            }

            return member;
        }

        String randomNickname;
        do {
            randomNickname = RandomNicknameCreator.createRandomNickname();
        } while (memberRepository.existsByNickname(randomNickname));

        Member newMember = Member.createOAuthMember(
            userInfo.getEmail(),
            AuthProvider.GOOGLE,
            randomNickname,
            userInfo.getProviderId());

        Member member = memberRepository.save(newMember);
        log.info("신규 OAuth2 사용자 생성: {}, provider: {}", userInfo.getEmail(), AuthProvider.GOOGLE);

        return member;
    }

    private String createAccessToken(Member member) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", member.getRole().getKey());
        return jwtProvider.generateAccessToken(member.getEmail(), claims);
    }

    private RefreshToken createRefreshToken(Member member) {
        CreateRefreshTokenRequest createRequest =
            new CreateRefreshTokenRequest(member.getEmail(), member.getId());
        return refreshTokenService.createRefreshToken(createRequest);
    }
}
