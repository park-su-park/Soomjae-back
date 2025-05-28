package com.parksupark.soomjae.server.member;

public enum Role {

    USER, ADMIN;

    public String getKey() {
        return "ROLE_" + this.name();
    }

}
