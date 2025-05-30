package com.parksupark.soomjae.server.auth.common.jwt.helper;

import com.parksupark.soomjae.server.auth.common.jwt.DefaultJwtGenerator;
import com.parksupark.soomjae.server.auth.common.jwt.DefaultJwtParser;
import com.parksupark.soomjae.server.auth.common.jwt.JwtProvider;
import com.parksupark.soomjae.server.auth.common.jwt.stub.ExpiredJwtGenerator;
import java.util.Base64;
import org.springframework.security.core.userdetails.UserDetailsService;

public class JwtTestHelper {

    public static final String SECRET = Base64.getEncoder()
        .encodeToString("test-secret-key-1234567890123456789012345678901234567890".getBytes());

    public static JwtProvider getDefaultFwtProvider(UserDetailsService userDetailsService) {
        return new JwtProvider(userDetailsService, new DefaultJwtGenerator(SECRET),
            new DefaultJwtParser(SECRET));
    }

    public static JwtProvider getExpiredJwtProvider(UserDetailsService userDetailsService) {
        return new JwtProvider(userDetailsService, new ExpiredJwtGenerator(SECRET),
            new DefaultJwtParser(SECRET));
    }

}
