package com.parksupark.soomjae.server.auth.common.jwt.stub;

import com.parksupark.soomjae.server.auth.common.jwt.AbstractJwtKeyHolder;
import com.parksupark.soomjae.server.auth.common.jwt.JwtGenerator;
import com.parksupark.soomjae.server.member.Role;
import io.jsonwebtoken.Jwts;
import java.util.Date;

public class ExpiredJwtGenerator extends AbstractJwtKeyHolder implements JwtGenerator {

    public ExpiredJwtGenerator(String secret) {
        super(secret);
    }

    @Override
    public String generate(String subject, Role role) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date expiry = new Date(nowMillis - 1000 * 60);

        return Jwts.builder()
            .subject(subject)
            .claim("role", role.getKey())
            .issuedAt(now)
            .expiration(expiry)
            .signWith(key)
            .compact();
    }
}
