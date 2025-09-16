package com.parksupark.soomjae.server.auth.jwt.refresh;

import com.parksupark.soomjae.server.auth.jwt.AbstractJwtKeyHolder;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;

public class ExpiredRefreshTokenGenerator extends AbstractJwtKeyHolder implements
    RefreshTokenGenerator {

    public ExpiredRefreshTokenGenerator(@Value("${jwt.refresh.secret}") String secret) {
        super(secret);
    }

    @Override
    public String generate(String subject) {
        long nowMillis = System.currentTimeMillis();
        Date expiry = new Date(nowMillis - 1000 * 60 * 60);

        return Jwts.builder()
            .subject(subject)
            .issuedAt(new Date(nowMillis - 1000 * 60 * 60 * 2))
            .expiration(expiry)
            .signWith(key)
            .compact();
    }
}
