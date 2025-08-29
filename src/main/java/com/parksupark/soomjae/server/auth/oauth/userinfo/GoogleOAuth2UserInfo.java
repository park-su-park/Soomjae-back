package com.parksupark.soomjae.server.auth.oauth.userinfo;

import java.util.Map;
import lombok.RequiredArgsConstructor;


/**
 * Google OAuth2 UserInfo 구현체
 * <p>Google OAuth2 API의 응답 구조에 마춰 사용자 정보를 추출</p>
 *
 * <pre>
 * Google 응답 예시:
 * {
 *      "sub": "1234567890,
 *      "email": "example@gmail.com",
 *      "email_verified": true,
 *      "name": "홍길동"
 * }
 * </pre>
 */
@RequiredArgsConstructor
public class GoogleOAuth2UserInfo implements OAuth2UserInfo {

    private final Map<String, Object> attributes;


    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get("sub");
    }

    @Override
    public boolean isEmailVerified() {
        return Boolean.TRUE.equals(attributes.get("email_verified"));
    }
}
