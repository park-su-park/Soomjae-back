package com.parksupark.soomjae.server.auth.common.jwt;

import static org.junit.jupiter.api.Assertions.*;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.Base64;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JwtProviderTest {

    private final String secret = Base64.getEncoder()
        .encodeToString("test-secret-key-1234567890123456789012345678901234567890".getBytes());

    private JwtProvider jwtProvider;

    @BeforeEach
    void setUp() {
        this.jwtProvider = new JwtProvider(secret);
        jwtProvider.init();
    }
    
    @Test
    void generator_shouldCreate_validSignedJWT() throws Exception {
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
    

}