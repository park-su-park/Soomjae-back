package com.parksupark.soomjae.server.auth.oauth.userinfo;

public interface OAuth2UserInfo {

    String getProviderId();

    String getEmail();
}
