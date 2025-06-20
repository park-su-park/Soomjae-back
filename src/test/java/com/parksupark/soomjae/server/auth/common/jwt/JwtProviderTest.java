package com.parksupark.soomjae.server.auth.common.jwt;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.parksupark.soomjae.server.auth.common.jwt.helper.JwtTestHelper;
import com.parksupark.soomjae.server.auth.username.dto.UsernamePasswordUserDetails;
import com.parksupark.soomjae.server.auth.username.service.UsernamePasswordUserDetailsService;
import com.parksupark.soomjae.server.member.Role;
import com.parksupark.soomjae.server.member.entity.Member;
import io.jsonwebtoken.Claims;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

@ExtendWith(MockitoExtension.class)
class JwtProviderTest {

    @Mock
    private UsernamePasswordUserDetailsService userDetailsService;

    private final Map<String, Object> claimsWithRole = new HashMap<>() {
        {
            put("role", Role.USER.getKey());
        }
    };

    @Test
    void generator_shouldCreate_validSignedJwt() {
        final String username = "test username";

        JwtProvider jwtProvider = JwtTestHelper.getDefaultFwtProvider(userDetailsService);

        String token = jwtProvider.generateToken(username, claimsWithRole);

        Claims claims = jwtProvider.getClaimsFromToken(token);

        System.out.println("token: " + token);
        System.out.println("issued at: " + claims.getIssuedAt());
        System.out.println("expiration: " + claims.getExpiration());
        assertEquals(username, claims.getSubject());
    }

    @Test
    void getAuthentication_withValidToken_shouldReturnAuthentication() throws Exception {
        final String username = "testusername";
        final String nickname = "testnickname";

        JwtProvider jwtProvider = JwtTestHelper.getDefaultFwtProvider(userDetailsService);

        String token = jwtProvider.generateToken(username, claimsWithRole);

        UsernamePasswordUserDetails userDetails = new UsernamePasswordUserDetails(
            Member.create(username, "password", nickname));
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);

        Authentication authentication = jwtProvider.getAuthentication(token);
        assertNotNull(authentication);
        assertEquals(userDetails, authentication.getPrincipal());
        assertEquals("", authentication.getCredentials());
        System.out.println("authentication.getAuthorities() = " + authentication.getAuthorities());
    }

    @Test
    void validateToken_withExpiredToken_shouldReturnExpiredJwtException() {
        final String username = "expired_user";

        JwtProvider jwtProvider = JwtTestHelper.getExpiredJwtProvider(userDetailsService);
        String token = jwtProvider.generateToken(username, claimsWithRole);

        assertFalse(jwtProvider.validateToken(token));
    }
}