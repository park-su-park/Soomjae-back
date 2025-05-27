package com.parksupark.soomjae.server.community.community_post.dto;

import com.parksupark.soomjae.server.community.community_post.entity.CommunityPost;
import com.parksupark.soomjae.server.member.entity.Member;
import lombok.Getter;

@Getter
public class CommunityPostRequest {

    private String title;
    private String content;

    public  CommunityPost toEntity(Member member) {
        return CommunityPost.builder()
            .title(title)
            .content(content)
            .member(member)
            .build();
    }
}
