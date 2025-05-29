package com.parksupark.soomjae.server.auth.common.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DefaultJwtParser extends AbstractJwtKeyHolder implements JwtParser {

    public DefaultJwtParser(@Value("{jwt.secret}") String secret) {
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
