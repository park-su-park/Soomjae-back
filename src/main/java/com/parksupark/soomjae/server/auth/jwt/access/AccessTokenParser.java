package com.parksupark.soomjae.server.auth.jwt.access;

import io.jsonwebtoken.Claims;

public interface AccessTokenParser {

    Claims parse(String token);
}
