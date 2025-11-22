package com.parksupark.soomjae.server.community.post.introducepost.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class CreateIntroducePostRequest {

    private final String content;

    @JsonCreator
    public CreateIntroducePostRequest(
        @JsonProperty("content") String content
    ) {
        this.content = content;
    }

}
