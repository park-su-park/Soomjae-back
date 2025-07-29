package com.parksupark.soomjae.server.auth.jwt.stub;

import com.parksupark.soomjae.server.auth.jwt.AbstractJwtKeyHolder;
import com.parksupark.soomjae.server.auth.jwt.access.AccessTokenGenerator;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import java.util.Map;

public class ExpiredAccessTokenGenerator extends AbstractJwtKeyHolder implements AccessTokenGenerator {

    public ExpiredAccessTokenGenerator(String secret) {
        super(secret);
    }

    @Override
    public String generate(String subject, Map<String, Object> claims) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date expiry = new Date(nowMillis - 1000 * 60);

        return Jwts.builder()
            .subject(subject)
            .claims(claims)
            .issuedAt(now)
            .expiration(expiry)
            .signWith(key)
            .compact();
    }
}
