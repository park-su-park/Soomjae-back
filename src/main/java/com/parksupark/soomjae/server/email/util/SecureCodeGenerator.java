package com.parksupark.soomjae.server.email.util;

import java.security.SecureRandom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecureCodeGenerator {

    private final SecureRandom secureRandom;
    private static final String SAFE_CHARS = "23456789ABCDEFGHJKMNPQRSTUVWXYZ";
    private static final int CODE_LENGTH = 6;

    public String generateVerificationCode() {
        StringBuilder code = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(SAFE_CHARS.charAt(secureRandom.nextInt(SAFE_CHARS.length())));
        }
        return code.toString();
    }
}