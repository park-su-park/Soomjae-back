package com.parksupark.soomjae.server.member.util;

import java.security.SecureRandom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RandomNicknameCreator {

    private final SecureRandom secureRandom;

    public String createRandomNickname() {
        int randomSuffix = secureRandom.nextInt(100_000, 1_000_000);

        return "soomjae_user_" + randomSuffix;
    }
}
