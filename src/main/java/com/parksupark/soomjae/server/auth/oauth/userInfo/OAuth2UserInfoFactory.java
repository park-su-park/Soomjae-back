package com.parksupark.soomjae.server.auth.oauth.userInfo;

import com.parksupark.soomjae.server.auth.oauth.AuthProvider;
import com.parksupark.soomjae.server.auth.oauth.exception.OAuth2AuthenticationProcessingException;
import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(AuthProvider provider, Map<String, Object> attributes) {
        return switch (provider) {
            case GOOGLE -> new GoogleOAuth2UserInfo(attributes);
            default ->
                throw new OAuth2AuthenticationProcessingException(provider + " 로그인은 제공하지 않습니다.");
        };
    }

}
