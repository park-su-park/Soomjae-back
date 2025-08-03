package com.parksupark.soomjae.server.auth.username.filter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parksupark.soomjae.server.auth.common.exception.FilterAuthenticationFailedException;
import com.parksupark.soomjae.server.auth.jwt.JwtProvider;
import com.parksupark.soomjae.server.auth.jwt.service.NoOpRefreshTokenService;
import com.parksupark.soomjae.server.auth.jwt.service.RefreshTokenService;
import com.parksupark.soomjae.server.auth.username.dto.UsernamePasswordAuthSuccessResponse;
import com.parksupark.soomjae.server.auth.username.dto.UsernamePasswordLoginRequest;
import com.parksupark.soomjae.server.auth.username.dto.UsernamePasswordUserDetails;
import com.parksupark.soomjae.server.member.Role;
import com.parksupark.soomjae.server.member.entity.Member;
import jakarta.servlet.FilterChain;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

@ExtendWith(MockitoExtension.class)
class UsernamePasswordLoginFilterTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtProvider jwtProvider;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private UsernamePasswordLoginFilter filter;

    private final RefreshTokenService refreshTokenService = new NoOpRefreshTokenService();

    private final Map<String, Object> claimsWithRole = new HashMap<>() {
        {
            put("role", Role.USER.getKey());
        }
    };

    @BeforeEach
    void setUp() {
        filter = new UsernamePasswordLoginFilter(authenticationManager, objectMapper, jwtProvider,
            refreshTokenService, false);
    }


    @Test
    void attemptAuthentication_withValidRequest_shouldCallAuthenticationManager() throws Exception {

        final String testEmail = "test@example.com";
        final String testPassword = "testPassword";

        // 요청 생성
        UsernamePasswordLoginRequest loginRequest = new UsernamePasswordLoginRequest(
            testEmail, testPassword);

        String jsonRequest = new ObjectMapper().writeValueAsString(loginRequest);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCharacterEncoding("UTF-8");
        request.setContentType("application/json");
        request.setContent(jsonRequest.getBytes());
        
        // 응답 생성
        MockHttpServletResponse response = new MockHttpServletResponse();
        
        // 기대 결과 값
        UsernamePasswordAuthenticationToken expectedToken = new UsernamePasswordAuthenticationToken(
            testEmail, testPassword);

        when(authenticationManager.authenticate(any(Authentication.class)))
            .thenReturn(expectedToken);

        // when
        Authentication result = filter.attemptAuthentication(request, response);

        // then
        assertNotNull(result);
        assertEquals(testEmail, result.getPrincipal());
        assertEquals(testPassword, result.getCredentials());
        verify(authenticationManager, times(1)).authenticate(any(Authentication.class));
    }

    @Test
    void attemptAuthentication_withInvalidJson_throwsAuthenticationFailedException() {

        final String invalidJson = "{ bad json }";
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setContentType("application/json");
        request.setCharacterEncoding("UTF-8");
        request.setContent(invalidJson.getBytes());

        MockHttpServletResponse response = new MockHttpServletResponse();

        // when + then
        assertThrows(FilterAuthenticationFailedException.class, () -> {
            filter.attemptAuthentication(request, response);
        });
    }

    @Test
    void successfulAuthentication_withValidAuthentication_shouldWriteJwtToResponse()
        throws Exception {
        final String username = "test";
        final String fakeToken = "fake token";
        final MockHttpServletRequest request = new MockHttpServletRequest();
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final FilterChain filterChain = mock(FilterChain.class);
        final Member member = mock(Member.class);
        when(member.getId()).thenReturn(1L);
        when(member.getEmail()).thenReturn(username);
        when(member.getRole()).thenReturn(Role.USER);
        final Authentication authResult = mock(Authentication.class);

        when(authResult.getPrincipal()).thenReturn(new UsernamePasswordUserDetails(member));

        when(jwtProvider.generateAccessToken(username, claimsWithRole)).thenReturn(fakeToken);

        filter.successfulAuthentication(request, response, filterChain, authResult);

        UsernamePasswordAuthSuccessResponse successResponseForTest =
            new UsernamePasswordAuthSuccessResponse(fakeToken, 1L);
        String expectedResponseJson = objectMapper.writeValueAsString(successResponseForTest);
        assertEquals(expectedResponseJson, response.getContentAsString());
        assertEquals("UTF-8", response.getCharacterEncoding());
        assertEquals("application/json;charset=UTF-8", response.getContentType());

    }

}