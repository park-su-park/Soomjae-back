package com.parksupark.soomjae.server.community.post.memberpost.dto;

import com.parksupark.soomjae.server.community.post.memberpost.entity.MemberPost;
import com.parksupark.soomjae.server.member.dto.MemberResponse;
import com.parksupark.soomjae.server.member.entity.Member;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class MemberPostFeedResponse {

    private final Long memberPostId;

    private final MemberResponse author;

    private final Instant createdAt;

    private final String content;

    private final List<String> images = new ArrayList<>();

    private final boolean isLiked;

    private final Long likeCount;

    private final Long commentCount;

    public MemberPostFeedResponse(MemberPost memberPost, Member member, boolean isLiked,
        Long likeCount, Long commentCount) {
        this.memberPostId= memberPost.getId();
        this.author = MemberResponse.create(member);
        this.createdAt = memberPost.getCreatedTime();
        this.content = memberPost.getContent();
        memberPost.getImages().forEach(memberPostImage -> this.images.add(
            memberPostImage.getImageUrl()));
        this.isLiked = isLiked;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
    }

}
