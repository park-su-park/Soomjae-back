package com.parksupark.soomjae.server.auth.username.testconfig;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.parksupark.soomjae.server.auth.username.service.UsernamePasswordUserDetailsService;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class DaoAuthenticationProviderTest {

    @Mock
    private UsernamePasswordUserDetailsService userDetailsService;

    @Mock
    private PasswordEncoder passwordEncoder;

    private DaoAuthenticationProvider daoAuthenticationProvider;

    @BeforeEach
    void setUp() {
        daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
    }

    private final String email = "test@example.com";
    private final String rawPassword = "raw";
    private final String encodedPassword = "hashed";

    @Test
    void authenticate_withValidCredentials_returnsAuthentication() {

        // mocking
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getPassword()).thenReturn(encodedPassword);

        when(userDetails.isAccountNonLocked()).thenReturn(true);
        when(userDetails.isAccountNonExpired()).thenReturn(true);
        when(userDetails.isCredentialsNonExpired()).thenReturn(true);
        when(userDetails.isEnabled()).thenReturn(true);
        when(userDetails.getAuthorities()).thenReturn(Collections.emptyList());


        when(userDetailsService.loadUserByUsername(email))
            .thenReturn(userDetails);
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);

        // given
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(email,
            rawPassword);

        // when
        Authentication authenticate = daoAuthenticationProvider.authenticate(token);

        // then
        assertEquals(userDetails, authenticate.getPrincipal());
        assertEquals(rawPassword, authenticate.getCredentials());
        assertTrue(authenticate.isAuthenticated());
    }

    @Test
    void authenticate_withInvalidCredentials_returnsBadCredentialsException() {

        // mocking
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getPassword()).thenReturn(encodedPassword);

        when(userDetails.isAccountNonLocked()).thenReturn(true);
        when(userDetails.isAccountNonExpired()).thenReturn(true);
        when(userDetails.isEnabled()).thenReturn(true);

        when(userDetailsService.loadUserByUsername(email))
            .thenReturn(userDetails);
        // 비밀번호 불일치 설정
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(false);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(email,
            rawPassword);

        // when + then
        assertThrows(BadCredentialsException.class, () -> {
            daoAuthenticationProvider.authenticate(token);
        });

    }


}
