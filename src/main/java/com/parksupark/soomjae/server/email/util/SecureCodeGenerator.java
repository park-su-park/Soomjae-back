package com.parksupark.soomjae.server.email.util;

import java.security.SecureRandom;
import org.springframework.stereotype.Component;

@Component
public class SecureCodeGenerator {

    private final SecureRandom secureRandom = new SecureRandom();
    private static final String SAFE_CHARS = "23456789ABCDEFGHJKMNPQRSTUVWXYZ";

    public String generateVerificationCode() {
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            code.append(SAFE_CHARS.charAt(secureRandom.nextInt(SAFE_CHARS.length())));
        }
        return code.toString();
    }
}