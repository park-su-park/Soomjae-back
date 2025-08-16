package com.parksupark.soomjae.server.auth.oauth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parksupark.soomjae.server.auth.jwt.JwtProvider;
import com.parksupark.soomjae.server.auth.jwt.dto.CreateRefreshTokenRequest;
import com.parksupark.soomjae.server.auth.jwt.entity.RefreshToken;
import com.parksupark.soomjae.server.auth.jwt.service.RefreshTokenService;
import com.parksupark.soomjae.server.auth.oauth.dto.CustomOAuth2User;
import com.parksupark.soomjae.server.auth.oauth.dto.OAuth2AuthSuccessResponse;
import com.parksupark.soomjae.server.member.entity.Member;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;
    private final ObjectMapper objectMapper;

    @Value("${app.cookie.secure}")
    private boolean cookieSecure;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException {

        CustomOAuth2User oauth2User = (CustomOAuth2User) authentication.getPrincipal();
        Member member = oauth2User.getMember();

        log.info("OAuth2 로그인 성공: {}, provider: {}", member.getEmail(), member.getProvider());

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", member.getRole().getKey());

        String accessToken = jwtProvider.generateAccessToken(member.getEmail(), claims);

        CreateRefreshTokenRequest createRefreshTokenRequest =
            new CreateRefreshTokenRequest(member.getEmail(), member.getId());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(
            createRefreshTokenRequest);

        setCookie(response, refreshToken);

        OAuth2AuthSuccessResponse successResponse =
            new OAuth2AuthSuccessResponse(accessToken, member.getId());

        // JSON 응답 (기존과 동일)
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(response.getWriter(), successResponse);
    }

    private void setCookie(HttpServletResponse response, RefreshToken refreshToken) {
        ResponseCookie cookie = ResponseCookie.from("refresh_token", refreshToken.getToken())
            .httpOnly(true)
            .secure(cookieSecure)
            .sameSite("Strict")
            .maxAge(Duration.between(Instant.now(), refreshToken.getExpiresAt()))
            .path("/")
            .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }
}