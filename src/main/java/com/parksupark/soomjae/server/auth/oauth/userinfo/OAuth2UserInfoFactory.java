package com.parksupark.soomjae.server.auth.oauth.userinfo;

import com.parksupark.soomjae.server.auth.oauth.AuthProvider;
import com.parksupark.soomjae.server.auth.oauth.exception.OAuth2AuthenticationProcessingException;
import com.parksupark.soomjae.server.common.exception.ErrorMessages;
import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(AuthProvider provider,
        Map<String, Object> attributes) {

        return switch (provider) {
            case GOOGLE -> new GoogleOAuth2UserInfo(attributes);
            default -> throw new OAuth2AuthenticationProcessingException(
                ErrorMessages.OAUTH2_PROVIDER_NOT_SUPPORT_MESSAGE + provider);
        };
    }

}
