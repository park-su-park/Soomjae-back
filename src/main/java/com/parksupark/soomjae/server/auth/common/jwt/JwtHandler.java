package com.parksupark.soomjae.server.auth.common.jwt;

import com.parksupark.soomjae.server.member.Role;
import io.jsonwebtoken.Claims;

public interface JwtHandler {
    String generate(String subject, Role role);

    Claims parse(String token);
}
