package com.parksupark.soomjae.server.auth.common.jwt.helper;

import com.parksupark.soomjae.server.auth.common.jwt.DefaultJwtHandler;
import com.parksupark.soomjae.server.auth.common.jwt.JwtProvider;
import com.parksupark.soomjae.server.auth.common.jwt.stub.ExpiredJwtHandler;
import java.util.Base64;
import org.springframework.security.core.userdetails.UserDetailsService;

public class JwtTestHelper {

    public static final String SECRET = Base64.getEncoder()
        .encodeToString("test-secret-key-1234567890123456789012345678901234567890".getBytes());

    public static JwtProvider getDefaultFwtProvider(UserDetailsService userDetailsService) {
        return new JwtProvider(userDetailsService, new DefaultJwtHandler(SECRET));
    }

    public static JwtProvider getExpiredJwtProvider(UserDetailsService userDetailsService) {
        return new JwtProvider(userDetailsService, new ExpiredJwtHandler(SECRET));
    }

}
