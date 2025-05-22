package com.parksupark.soomjae.server.auth.common.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;

@Component
public class DefaultJwtHandler implements JwtHandler {
    private final SecretKey key;

    public DefaultJwtHandler(@Value("${jwt.secret}") String secret) {
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        this.key = new SecretKeySpec(keyBytes, "HmacSHA256");
    }

    @Override
    public String generate(String subject) {
        long nowMillis = System.currentTimeMillis();
        long expirationMillis = nowMillis + 1000 * 60 * 60;

        return Jwts.builder()
                .subject(subject)
                .signWith(key)
                .issuedAt(new Date(nowMillis))
                .expiration(new Date(expirationMillis))
                .compact();
    }

    @Override
    public Claims parse(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
