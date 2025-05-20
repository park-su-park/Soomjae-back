package com.parksupark.soomjae.server.auth.username.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UsernamePasswordLoginRequest {

    private final String username;
    private final String password;

    @JsonCreator
    public UsernamePasswordLoginRequest(
        @JsonProperty("username") String username,
        @JsonProperty("password") String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
