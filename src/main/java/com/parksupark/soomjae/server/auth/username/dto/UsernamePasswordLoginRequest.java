package com.parksupark.soomjae.server.auth.username.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UsernamePasswordLoginRequest {

    private final String email;
    private final String password;

    @JsonCreator
    public UsernamePasswordLoginRequest(
        @JsonProperty("email") String email,
        @JsonProperty("password") String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
