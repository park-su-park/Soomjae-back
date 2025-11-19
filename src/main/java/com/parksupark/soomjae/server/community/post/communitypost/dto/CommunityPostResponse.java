package com.parksupark.soomjae.server.community.post.communitypost.dto;

import static com.parksupark.soomjae.server.community.common.constant.PostConstant.COMMUNITY_POST_TYPE;

import com.parksupark.soomjae.server.community.post.common.dto.PostResponse;
import com.parksupark.soomjae.server.community.post.communitypost.entity.CommunityPost;
import com.parksupark.soomjae.server.member.dto.MemberResponse;
import jakarta.annotation.Nullable;
import java.time.Instant;
import lombok.Getter;

@Getter
public class CommunityPostResponse implements PostResponse {

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
    private final Long commentNum;

    public CommunityPostResponse(Long postId, String title, String content,
        MemberResponse author, @Nullable String category, @Nullable String location,
        Instant createdTime, Long likeNum, Boolean isLikedByMe, Long commentNum) {
        this.postId = postId;
        this.postType = COMMUNITY_POST_TYPE;
        this.title = title;
        this.content = content;
        this.author = author;
        this.category = category;
        this.location = location;
        this.createdTime = createdTime;
        this.likeNum = likeNum;
        this.isLikedByMe = isLikedByMe;
        this.commentNum = commentNum;
    }

    public static CommunityPostResponse of(CommunityPost communityPost,
        CommunityPostStatsResponse communityPostStatsResponse) {
        return new CommunityPostResponse(communityPost.getId(),
            communityPost.getTitle(), communityPost.getContent(),
            MemberResponse.create(communityPost.getMember()), communityPost.getCategory().getName(),
            communityPost.getLocation().getName(), communityPost.getCreatedTime(),
            communityPostStatsResponse.getLikeCount(), communityPostStatsResponse.isLikedByMe(),
            communityPostStatsResponse.getCommentCount());
    }
}
