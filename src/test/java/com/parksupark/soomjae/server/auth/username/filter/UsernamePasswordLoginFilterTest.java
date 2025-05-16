package com.parksupark.soomjae.server.auth.username.filter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parksupark.soomjae.server.auth.common.FilterAuthenticationFailedException;
import com.parksupark.soomjae.server.auth.username.dto.UsernamePasswordLoginRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
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

    @InjectMocks
    private UsernamePasswordLoginFilter filter;

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
    void attemptAuthentication_withInvalidJson_throwsAuthenticationFailedException() throws Exception {

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

}