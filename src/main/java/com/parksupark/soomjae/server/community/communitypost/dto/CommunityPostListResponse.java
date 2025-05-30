package com.parksupark.soomjae.server.community.communitypost.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class CommunityPostListResponse {

    List<CommunityPostResponse> posts;

    public CommunityPostListResponse(List<CommunityPostResponse> posts) {
        this.posts = posts;
    }
}
