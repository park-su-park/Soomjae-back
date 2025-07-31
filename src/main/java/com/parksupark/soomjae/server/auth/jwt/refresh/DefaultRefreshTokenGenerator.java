package com.parksupark.soomjae.server.auth.jwt.refresh;

import com.parksupark.soomjae.server.auth.jwt.AbstractJwtKeyHolder;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DefaultRefreshTokenGenerator extends AbstractJwtKeyHolder implements
        RefreshTokenGenerator {

    private static final int WEEK_IN_MILLISECONDS = 1000 * 60 * 60 * 24 * 7;

    public DefaultRefreshTokenGenerator(@Value("${jwt.refresh.secret}") String secret) {
        super(secret);
    }

    @Override
    public String generate(String subject) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date expiry = new Date(nowMillis + WEEK_IN_MILLISECONDS);

        return Jwts.builder()
                .subject(subject)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(key)
                .compact();
    }
}
