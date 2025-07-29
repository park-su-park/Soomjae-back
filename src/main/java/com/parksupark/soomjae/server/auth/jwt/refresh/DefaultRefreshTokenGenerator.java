package com.parksupark.soomjae.server.auth.jwt.refresh;

import com.parksupark.soomjae.server.auth.jwt.AbstractJwtKeyHolder;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DefaultRefreshTokenGenerator extends AbstractJwtKeyHolder implements
        RefreshTokenGenerator {

    public DefaultRefreshTokenGenerator(@Value("${jwt.refresh.secret}") String secret) {
        super(secret);
    }

    @Override
    public String generate(String subject) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date expiry = new Date(nowMillis + 1000 * 60 * 60 * 24 * 7);

        return Jwts.builder()
                .subject(subject)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(key)
                .compact();
    }
}
