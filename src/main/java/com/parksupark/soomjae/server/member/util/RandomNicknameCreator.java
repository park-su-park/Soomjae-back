package com.parksupark.soomjae.server.member.util;

import java.util.Random;

public class RandomNicknameCreator {

    public static String createRandomNickname() {
        int randomSuffix = new Random().nextInt(900000) + 100000;

        return "soomjae_user_" + randomSuffix;
    }
}
