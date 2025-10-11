package com.parksupark.soomjae.server.community.post.memberpost.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class MemberPostIdResponse {

    private final Long memberPostId;

    @JsonCreator
    public MemberPostIdResponse(
        @JsonProperty("memberPostId") Long memberPostId) {
        this.memberPostId = memberPostId;
    }

}
