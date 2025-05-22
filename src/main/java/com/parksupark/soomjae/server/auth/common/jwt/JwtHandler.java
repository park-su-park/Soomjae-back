package com.parksupark.soomjae.server.auth.common.jwt;

import io.jsonwebtoken.Claims;

public interface JwtHandler {
    String generate(String subject);

    Claims parse(String token);
}
