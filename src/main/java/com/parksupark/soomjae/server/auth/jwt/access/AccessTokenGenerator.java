package com.parksupark.soomjae.server.auth.jwt.access;

import java.util.Map;

public interface AccessTokenGenerator {

    String generate(String subject, Map<String, Object> claims);
}
