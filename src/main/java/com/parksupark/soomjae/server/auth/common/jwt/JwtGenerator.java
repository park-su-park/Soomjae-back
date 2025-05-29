package com.parksupark.soomjae.server.auth.common.jwt;

import com.parksupark.soomjae.server.member.Role;

public interface JwtGenerator {

    String generate(String subject, Role role);
}
