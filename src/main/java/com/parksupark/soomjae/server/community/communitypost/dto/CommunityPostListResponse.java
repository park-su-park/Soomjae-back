package com.parksupark.soomjae.server.community.communitypost.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommunityPostListResponse {

    List<CommunityPostResponse> posts;

    public static CommunityPostListResponse of(List<CommunityPostResponse> posts) {
        return new CommunityPostListResponse(posts);
    }
}
