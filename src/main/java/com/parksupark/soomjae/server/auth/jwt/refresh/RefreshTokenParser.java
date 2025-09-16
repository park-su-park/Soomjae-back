package com.parksupark.soomjae.server.auth.jwt.refresh;

import io.jsonwebtoken.Claims;

public interface RefreshTokenParser {

    Claims parse(String token);
}
