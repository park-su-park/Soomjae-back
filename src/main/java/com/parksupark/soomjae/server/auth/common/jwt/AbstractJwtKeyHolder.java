package com.parksupark.soomjae.server.auth.common.jwt;

import java.util.Base64;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public abstract class AbstractJwtKeyHolder {

    protected final SecretKey key;

    protected AbstractJwtKeyHolder(String secret) {
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        this.key = new SecretKeySpec(keyBytes, "HmacSHA256");
    }
}
