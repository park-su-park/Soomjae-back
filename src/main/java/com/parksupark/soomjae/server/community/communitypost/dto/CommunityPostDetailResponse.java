package com.parksupark.soomjae.server.community.communitypost.dto;

import com.parksupark.soomjae.server.community.comment.dto.CommentResponse;
import com.parksupark.soomjae.server.community.communitypost.entity.CommunityPost;
import com.parksupark.soomjae.server.member.dto.MemberResponse;
import java.time.Instant;
import java.util.List;
import lombok.Getter;

@Getter
public class CommunityPostDetailResponse {

    private final Long postId;

    private final String postType;

    private final String title;

    private final String content;

    private final MemberResponse author;

    private final String category;

    private final String location;

    private final Instant createdTime;

    private final List<CommentResponse> comments;


    private CommunityPostDetailResponse(CommunityPost communityPost,
        List<CommentResponse> comments) {
        this.postId = communityPost.getId();
        this.postType = "community";
        this.title = communityPost.getTitle();
        this.content = communityPost.getContent();
        this.author = MemberResponse.create(communityPost.getMember());
        this.createdTime = communityPost.getCreatedTime();
        this.category =
            communityPost.getCategory() != null ? communityPost.getCategory().getName() : null;
        this.location =
            communityPost.getLocation() != null ? communityPost.getLocation().getName() : null;
        this.comments = comments;
    }

    public static CommunityPostDetailResponse of(CommunityPost communityPost,
        List<CommentResponse> comments) {
        return new CommunityPostDetailResponse(communityPost, comments);
    }
}
