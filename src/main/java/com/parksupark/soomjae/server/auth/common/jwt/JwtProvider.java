package com.parksupark.soomjae.server.auth.common.jwt;

import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtProvider {

    private final String secret;
    private Key key;

    public JwtProvider(@Value("${jwt.secret}") String secret) {
        this.secret = secret;
    }

    @PostConstruct
    public void init() {
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        this.key = new SecretKeySpec(keyBytes, "HmacSHA256");
    }

    public String generateToken(String subject) {

        long nowMillis = System.currentTimeMillis();
        long expirationMillis = nowMillis + 1000 * 60 * 60;

        return Jwts.builder()
            .subject(subject)
            .signWith(key)
            .issuedAt(new Date(nowMillis))
            .expiration(new Date(expirationMillis))
            .compact();
    }

}
