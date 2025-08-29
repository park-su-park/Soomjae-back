package com.parksupark.soomjae.server.auth.oauth.userinfo;

public interface OAuth2UserInfo {

    /**
     * OAuth2 Provider가 제공하는 고유 사용자 ID를 반환
     * @return Provider별 사용자 고유 ID
     */
    String getProviderId();

    /**
     * 사용자의 이메일 주소를 반환
     * @return 사용자 이메일 주소를 반환
     */
    String getEmail();

    /**
     * OAuth2 Provider에서 해당 이메일이 인증되었는지 여부를 반환
     * @return 이메일 인증 여부 (true: 인증됨, false: 미인증)
     */
    boolean isEmailVerified();
}
