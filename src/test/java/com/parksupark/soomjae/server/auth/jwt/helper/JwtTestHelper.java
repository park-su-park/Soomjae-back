package com.parksupark.soomjae.server.auth.jwt.helper;

import com.parksupark.soomjae.server.auth.jwt.access.DefaultAccessTokenGenerator;
import com.parksupark.soomjae.server.auth.jwt.access.DefaultAccessTokenParser;
import com.parksupark.soomjae.server.auth.jwt.refresh.DefaultRefreshTokenGenerator;
import com.parksupark.soomjae.server.auth.jwt.JwtProvider;
import com.parksupark.soomjae.server.auth.jwt.refresh.DefaultRefreshTokenParser;
import com.parksupark.soomjae.server.auth.jwt.stub.ExpiredAccessTokenGenerator;
import java.util.Base64;

import org.springframework.security.core.userdetails.UserDetailsService;

public class JwtTestHelper {

    public static final String SECRET = Base64.getEncoder()
        .encodeToString("test-secret-key-1234567890123456789012345678901234567890".getBytes());

    public static JwtProvider getDefaultFwtProvider(UserDetailsService userDetailsService) {
        return new JwtProvider(userDetailsService, new DefaultAccessTokenGenerator(SECRET), new DefaultRefreshTokenGenerator(SECRET),
                new DefaultAccessTokenParser(SECRET), new DefaultRefreshTokenParser(SECRET));
    }

    public static JwtProvider getExpiredJwtProvider(UserDetailsService userDetailsService) {
        return new JwtProvider(userDetailsService, new ExpiredAccessTokenGenerator(SECRET), new DefaultRefreshTokenGenerator(SECRET),
                new DefaultAccessTokenParser(SECRET), new DefaultRefreshTokenParser(SECRET));
    }

}
