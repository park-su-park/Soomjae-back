package com.parksupark.soomjae.server.auth.jwt.refresh;

import com.parksupark.soomjae.server.auth.jwt.AbstractJwtKeyHolder;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DefaultRefreshTokenParser extends AbstractJwtKeyHolder implements RefreshTokenParser {

    public DefaultRefreshTokenParser(@Value("${jwt.refresh.secret}") String secret) {
        super(secret);
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
