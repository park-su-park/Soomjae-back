package com.parksupark.soomjae.server.community.post.common.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PostListResponse {

    List<? extends PostResponse> posts;

    public static PostListResponse of(List<? extends PostResponse> posts) {
        return new PostListResponse(posts);
    }
}
