package com.parksupark.soomjae.server.auth.username.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parksupark.soomjae.server.auth.common.exception.FilterAuthenticationFailedException;
import com.parksupark.soomjae.server.auth.jwt.JwtProvider;
import com.parksupark.soomjae.server.auth.jwt.dto.CreateRefreshTokenRequest;
import com.parksupark.soomjae.server.auth.jwt.entity.RefreshToken;
import com.parksupark.soomjae.server.auth.jwt.service.RefreshTokenService;
import com.parksupark.soomjae.server.auth.username.dto.UsernamePasswordAuthSuccessResponse;
import com.parksupark.soomjae.server.auth.username.dto.UsernamePasswordLoginRequest;
import com.parksupark.soomjae.server.auth.username.dto.UsernamePasswordUserDetails;
import com.parksupark.soomjae.server.member.Role;
import com.parksupark.soomjae.server.member.entity.Member;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
public class UsernamePasswordLoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    public UsernamePasswordLoginFilter(AuthenticationManager authenticationManager,
        ObjectMapper objectMapper, JwtProvider jwtProvider, RefreshTokenService refreshTokenService) {
        this.authenticationManager = authenticationManager;
        this.objectMapper = objectMapper;
        this.jwtProvider = jwtProvider;
        this.refreshTokenService = refreshTokenService;
        super.setFilterProcessesUrl("/v1/auth/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
        HttpServletResponse response) throws AuthenticationException {

        try {
            UsernamePasswordLoginRequest loginRequest = objectMapper.readValue(request.getReader(),
                UsernamePasswordLoginRequest.class);

            String principal = loginRequest.getEmail();
            String credential = loginRequest.getPassword();

            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                principal, credential);

            return authenticationManager.authenticate(token);

        } catch (IOException e) {
            throw new FilterAuthenticationFailedException(
                "error occurred while processing username/password authorization", e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, FilterChain chain, Authentication authResult)
        throws IOException, ServletException {
        Map<String, Object> claims = new HashMap<>();

        UsernamePasswordUserDetails userDetails =
            (UsernamePasswordUserDetails) authResult.getPrincipal();
        Member member = userDetails.getMember();

        String username = userDetails.getUsername();
        Role role = member.getRole();

        claims.put("role", role.getKey());

        String accessToken = jwtProvider.generateAccessToken(username, claims);

        // refresh token 생성
        CreateRefreshTokenRequest createRefreshTokenRequest = new CreateRefreshTokenRequest(username, member.getId());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(createRefreshTokenRequest);

        // 쿠키 설정
        setCookie(response, refreshToken);

        UsernamePasswordAuthSuccessResponse successResponse =
            new UsernamePasswordAuthSuccessResponse(accessToken, member.getId());

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        objectMapper.writeValue(response.getWriter(), successResponse);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, AuthenticationException failed)
        throws IOException, ServletException {

        log.warn("username/password login failed: {}", failed.toString());

        // 로그인 실패 응답 처리
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        // 응답 바디는 추후 구현
    }

    private void setCookie(HttpServletResponse response, RefreshToken refreshToken) {
        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken.getToken())
                .httpOnly(true)
                // 개발 환경에선 false
                .secure(false)
                .sameSite("Strict")
                .maxAge(Duration.between(Instant.now(), refreshToken.getExpiresAt()))
                .path("/")
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }
}
