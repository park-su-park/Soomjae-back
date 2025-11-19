package com.parksupark.soomjae.server.community.post.communitypost.dto;

import com.parksupark.soomjae.server.community.comment.dto.CommentResponse;
import com.parksupark.soomjae.server.community.common.constant.PostConstant;
import com.parksupark.soomjae.server.community.post.communitypost.entity.CommunityPost;
import com.parksupark.soomjae.server.member.dto.MemberResponse;
import jakarta.annotation.Nullable;
import java.time.Instant;
import java.util.List;
import lombok.Getter;

@Getter
public class CommunityPostResponseWithComments {

    private final Long postId;
    private final String postType;
    private final String title;
    private final String content;
    private final MemberResponse author;
    @Nullable
    private final String category;
    @Nullable
    private final String location;
    private final Instant createdTime;
    private final Long likeNum;
    private final Boolean isLikedByMe;

    private final List<CommentResponse> comments;

    private CommunityPostResponseWithComments(CommunityPost communityPost, Long likeNum,
        Boolean isLikedByMe, List<CommentResponse> comments) {
        this.postId = communityPost.getId();
        this.postType = PostConstant.COMMUNITY_POST_TYPE;
        this.title = communityPost.getTitle();
        this.content = communityPost.getContent();
        this.author = MemberResponse.create(communityPost.getMember());
        this.category = communityPost.getCategory().getName();
        this.location = communityPost.getLocation().getName();
        this.createdTime = communityPost.getCreatedTime();
        this.likeNum = likeNum;
        this.isLikedByMe = isLikedByMe;
        this.comments = comments;

    }

    public static CommunityPostResponseWithComments of(CommunityPost communityPost, Long likeNum,
        Boolean isLikedByMe, List<CommentResponse> comments) {
        return new CommunityPostResponseWithComments(communityPost, likeNum, isLikedByMe, comments);
    }
}