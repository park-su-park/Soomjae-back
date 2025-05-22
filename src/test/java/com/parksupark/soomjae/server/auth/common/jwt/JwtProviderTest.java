package com.parksupark.soomjae.server.auth.common.jwt;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.parksupark.soomjae.server.auth.username.dto.UsernamePasswordUserDetails;
import com.parksupark.soomjae.server.auth.username.service.UsernamePasswordUserDetailsService;
import com.parksupark.soomjae.server.member.entity.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken. Jwts;
import java.util.Base64;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

@ExtendWith(MockitoExtension.class)
class JwtProviderTest {

    private final String secret = Base64.getEncoder()
        .encodeToString("test-secret-key-1234567890123456789012345678901234567890".getBytes());

    private JwtProvider jwtProvider;


    @Mock
    private UsernamePasswordUserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        JwtHandler jwtHandler = new DefaultJwtHandler(secret);
        this.jwtProvider = new JwtProvider(userDetailsService, jwtHandler);
    }
    
    @Test
    void generator_shouldCreate_validSignedJwt() throws Exception {
        final String username = "test username";

        byte[] keyBytes = Base64.getDecoder().decode(secret);
        SecretKey key = new SecretKeySpec(keyBytes, "HmacSHA256");

        String token = jwtProvider.generateToken(username);

        Claims claims = Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .getPayload();

        System.out.println("token: " + token);
        System.out.println("issued at: " + claims.getIssuedAt());
        System.out.println("expiration: " + claims.getExpiration());
        assertEquals(username, claims.getSubject());
    }
    
    @Test
    void getAuthentication_withValidToken_shouldReturnAuthentication() throws Exception {
        final String username = "test username";

        byte[] keyBytes = Base64.getDecoder().decode(secret);
        SecretKey key = new SecretKeySpec(keyBytes, "HmacSHA256");

        String token = jwtProvider.generateToken(username);

        UsernamePasswordUserDetails userDetails = new UsernamePasswordUserDetails(Member.create(username, "password"));
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);

        Authentication authentication = jwtProvider.getAuthentication(token);
        assertNotNull(authentication);
        assertEquals(userDetails, authentication.getPrincipal());
        assertEquals("", authentication.getCredentials());
        System.out.println("authentication.getAuthorities() = " + authentication.getAuthorities());
    }
    

}