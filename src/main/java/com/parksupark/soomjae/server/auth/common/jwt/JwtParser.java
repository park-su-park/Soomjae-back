package com.parksupark.soomjae.server.auth.common.jwt;

import io.jsonwebtoken.Claims;

public interface JwtParser {

    Claims parse(String token);
}
