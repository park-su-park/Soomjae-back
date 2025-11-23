package com.parksupark.soomjae.server.community.post.introductionpost.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class UpdateIntroductionPostRequest {

    private final String content;

    @JsonCreator
    public UpdateIntroductionPostRequest(
        @JsonProperty("content") String content
    ) {
        this.content = content;
    }

}
