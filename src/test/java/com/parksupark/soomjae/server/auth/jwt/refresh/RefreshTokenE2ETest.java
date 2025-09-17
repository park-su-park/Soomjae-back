package com.parksupark.soomjae.server.auth.jwt.refresh;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parksupark.soomjae.server.auth.common.exception.RefreshFailedException;
import com.parksupark.soomjae.server.auth.jwt.JwtProvider;
import com.parksupark.soomjae.server.auth.username.dto.UsernamePasswordAuthSuccessResponse;
import com.parksupark.soomjae.server.auth.username.dto.UsernamePasswordLoginRequest;
import com.parksupark.soomjae.server.common.exception.ErrorMessages;
import com.parksupark.soomjae.server.member.dto.CreateMemberRequest;
import com.parksupark.soomjae.server.member.service.MemberService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class RefreshTokenE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberService memberService;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${jwt.refresh.secret}")
    private String refreshSecret;

    private static final String REFRESH_URI = "/v1/auth/refresh";

    private final String email = "test@example.com";
    private final String password = "test";
    private final String nickname = "test";

    private String refreshToken;

    private ExpiredRefreshTokenGenerator expiredRefreshTokenGenerator;


    @BeforeEach
    void init() throws Exception {
        expiredRefreshTokenGenerator = new ExpiredRefreshTokenGenerator(refreshSecret);

        // 1. 회원 생성
        CreateMemberRequest request = new CreateMemberRequest(email, password, nickname);
        memberService.createMember(request);

        // 2. 로그인
        UsernamePasswordLoginRequest loginRequest = new UsernamePasswordLoginRequest(email,
            password);

        MvcResult loginResult = mockMvc.perform(post("/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isOk())
            .andReturn();

        // 3. refresh token 추출
        refreshToken = extractRefreshTokenFromCookie(loginResult);
    }

    @Test
    @DisplayName("/auth/refresh 성공 케이스")
    void refreshSuccessCase() throws Exception {

        // when + then
        MvcResult result = mockMvc.perform(post("/v1/auth/refresh")
                .cookie(new Cookie("refresh_token", refreshToken)))
            .andExpect(status().isOk())
            .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        UsernamePasswordAuthSuccessResponse response = objectMapper.readValue(responseBody,
            UsernamePasswordAuthSuccessResponse.class);

        assertThat(response.getAccessToken()).isNotNull();
        assertThat(jwtProvider.validateAccessToken(response.getAccessToken())).isTrue();
    }

    @Test
    @DisplayName("만료된 refresh token을 사용하여 refresh 요청")
    void refresh_withExpiredRefreshToken() throws Exception {

        // when + then
        String expiredRefreshToken = expiredRefreshTokenGenerator.generate(email);

        MvcResult result = mockMvc.perform(post(REFRESH_URI)
                .cookie(new Cookie("refresh_token", expiredRefreshToken)))
            .andExpect(status().isUnauthorized())
            .andReturn();

        Cookie[] cookies = result.getResponse().getCookies();
        Cookie refreshTokenCookie = null;

        for (Cookie cookie : cookies) {
            if ("refresh_token".equals(cookie.getName())) {
                refreshTokenCookie = cookie;
                break;
            }
        }

        assertThat(refreshTokenCookie).isNotNull();
        assertThat(refreshTokenCookie.getValue()).isEmpty();
        assertThat(refreshTokenCookie.getMaxAge()).isZero();
    }

    private String extractRefreshTokenFromCookie(MvcResult mvcResult) {
        Cookie[] cookies = mvcResult.getResponse().getCookies();

        for (Cookie cookie : cookies) {
            if ("refresh_token".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        throw new RefreshFailedException(ErrorMessages.REFRESH_TOKEN_NOT_FOUND_FROM_COOKIE_MESSAGE);
    }
}
