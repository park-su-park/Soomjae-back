package com.parksupark.soomjae.server.auth.common.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class DefaultJwtHandler extends AbstractJwtHandler {

    public DefaultJwtHandler(@Value("${jwt.secret}") String secret) {
        super(secret);
    }

    @Override
    protected Date getIssuedAt() {
        return new Date();
    }

    @Override
    protected Date getExpiration() {
        return new Date(System.currentTimeMillis() + 3600000); // 1시간 후
    }
}
