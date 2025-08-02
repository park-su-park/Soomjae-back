package com.parksupark.soomjae.server.community.communitypost.dto;

import com.parksupark.soomjae.server.community.communitypost.entity.CommunityPost;
import com.parksupark.soomjae.server.member.dto.MemberResponse;
import java.time.Instant;
import lombok.Getter;

@Getter
public abstract class CommunityPostBaseResponse {

    private final Long postId;
    private final String postType;
    private final String title;
    private final String content;
    private final MemberResponse author;
    private final String category;
    private final String location;
    private final Instant createdTime;
    private final Long likeNum;
    private final Boolean isLikedByMe;

    protected CommunityPostBaseResponse(CommunityPost communityPost, Long likeNum,
        Boolean isLikedByMe) {
        this.postId = communityPost.getId();
        this.postType = "community";
        this.title = communityPost.getTitle();
        this.content = communityPost.getContent();
        this.author = MemberResponse.of(communityPost.getMember());
        this.createdTime = communityPost.getCreatedTime();
        this.category =
            communityPost.getCategory() != null ? communityPost.getCategory().getName() : null;
        this.location =
            communityPost.getLocation() != null ? communityPost.getLocation().getName() : null;
        this.likeNum = likeNum;
        this.isLikedByMe = isLikedByMe;
    }
}
