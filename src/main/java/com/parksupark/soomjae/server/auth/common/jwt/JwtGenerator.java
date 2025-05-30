package com.parksupark.soomjae.server.auth.common.jwt;

import java.util.Map;

public interface JwtGenerator {

    String generate(String subject, Map<String, Object> claims);
}
