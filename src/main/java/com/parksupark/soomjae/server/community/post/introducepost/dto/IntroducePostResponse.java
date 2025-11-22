package com.parksupark.soomjae.server.community.post.introducepost.dto;

import com.parksupark.soomjae.server.community.post.common.dto.PostResponse;
import com.parksupark.soomjae.server.member.dto.MemberResponse;
import java.time.Instant;
import lombok.Getter;

@Getter
public class IntroducePostResponse implements PostResponse {

    private final Long postId;
    private final MemberResponse author;
    private final String content;
    private final Instant createdTime;

    public IntroducePostResponse(Long postId, MemberResponse author, String content,
        Instant createdTime) {
        this.postId = postId;
        this.author = author;
        this.content = content;
        this.createdTime = createdTime;
    }

}
