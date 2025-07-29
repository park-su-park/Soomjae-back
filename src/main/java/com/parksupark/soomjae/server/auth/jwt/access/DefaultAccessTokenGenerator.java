package com.parksupark.soomjae.server.auth.jwt.access;

import com.parksupark.soomjae.server.auth.jwt.AbstractJwtKeyHolder;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DefaultAccessTokenGenerator extends AbstractJwtKeyHolder implements AccessTokenGenerator {

    public DefaultAccessTokenGenerator(@Value("${jwt.secret}") String secret) {
        super(secret);
    }

    @Override
    public String generate(String subject, Map<String, Object> claims) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date expiry = new Date(nowMillis + 1000 * 60 * 60);

        return Jwts.builder()
            .subject(subject)
            .claims(claims)
            .issuedAt(now)
            .expiration(expiry)
            .signWith(key)
            .compact();
    }
}
