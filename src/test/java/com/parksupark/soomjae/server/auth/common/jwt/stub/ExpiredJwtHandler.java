package com.parksupark.soomjae.server.auth.common.jwt.stub;

import com.parksupark.soomjae.server.auth.common.jwt.AbstractJwtHandler;
import java.util.Date;

public class ExpiredJwtHandler extends AbstractJwtHandler {

    public ExpiredJwtHandler(String secret) {
        super(secret);
    }

    @Override
    protected Date getIssuedAt() {
        return new Date(System.currentTimeMillis() - 2000);
    }

    @Override
    protected Date getExpiration() {
        return new Date(System.currentTimeMillis() - 1000);
    }
}
