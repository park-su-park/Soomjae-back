package com.parksupark.soomjae.server.auth.username.filter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parksupark.soomjae.server.auth.common.exception.FilterAuthenticationFailedException;
import com.parksupark.soomjae.server.auth.common.jwt.JwtProvider;
import com.parksupark.soomjae.server.auth.username.dto.UsernamePasswordAuthSuccessResponse;
import com.parksupark.soomjae.server.auth.username.dto.UsernamePasswordLoginRequest;
import jakarta.servlet.FilterChain;
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

    @BeforeEach
    void setUp() {
        filter = new UsernamePasswordLoginFilter(authenticationManager, objectMapper, jwtProvider);
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
    void successfulAuthentication_withValidAuthentication_shouldWriteJWTToResponse()
        throws Exception {
        final String username = "test";
        final String fakeToken = "fake token";

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        Authentication authResult = mock(Authentication.class);

        when(authResult.getName()).thenReturn(username);

        when(jwtProvider.generateToken(username)).thenReturn(fakeToken);

        filter.successfulAuthentication(request, response, filterChain, authResult);

        String expectedResponseJson = objectMapper.writeValueAsString(new UsernamePasswordAuthSuccessResponse(fakeToken));
        assertEquals(response.getContentAsString(), expectedResponseJson);
        assertEquals("UTF-8", response.getCharacterEncoding());
        assertEquals("application/json;charset=UTF-8", response.getContentType());

    }

}