package com.parksupark.soomjae.server.auth.common.jwt;

import com.parksupark.soomjae.server.member.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.Base64;
import java.util.Date;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public abstract class AbstractJwtHandler implements JwtHandler {

    protected final SecretKey key;

    protected AbstractJwtHandler(String secret) {
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        this.key = new SecretKeySpec(keyBytes, "HmacSHA256");
    }

    @Override
    public Claims parse(String token) {
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    protected abstract Date getIssuedAt();

    protected abstract Date getExpiration();

    @Override
    public String generate(String subject, Role role) {
        return Jwts.builder()
            .subject(subject)
            .claim("role", role.getKey())
            .issuedAt(getIssuedAt())
            .expiration(getExpiration())
            .signWith(key)
            .compact();
    }
}
