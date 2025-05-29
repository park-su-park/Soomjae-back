package com.parksupark.soomjae.server.auth.common.jwt;

import com.parksupark.soomjae.server.member.Role;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DefaultJwtGenerator extends AbstractJwtKeyHolder implements JwtGenerator {

    public DefaultJwtGenerator(@Value("${jwt.secret}") String secret) {
        super(secret);
    }

    @Override
    public String generate(String subject, Role role) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date expiry = new Date(nowMillis + 1000 * 60 * 60);

        return Jwts.builder()
            .subject(subject)
            .claim("role", role.getKey())
            .issuedAt(now)
            .expiration(expiry)
            .signWith(key)
            .compact();
    }
}
