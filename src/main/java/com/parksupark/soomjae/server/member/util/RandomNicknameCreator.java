package com.parksupark.soomjae.server.member.util;

import java.security.SecureRandom;

public class RandomNicknameCreator {

    // SecureRandom 인스턴스는 생성 비용이 비싸므로 재사용
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public static String createRandomNickname(){
        int randomSuffix = SECURE_RANDOM.nextInt(900000) + 100000;

        return "soomjae_user_" + randomSuffix;
    }

}
