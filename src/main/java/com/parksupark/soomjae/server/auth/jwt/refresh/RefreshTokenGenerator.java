package com.parksupark.soomjae.server.auth.jwt.refresh;

public interface RefreshTokenGenerator {
    String generate(String subject);
}
