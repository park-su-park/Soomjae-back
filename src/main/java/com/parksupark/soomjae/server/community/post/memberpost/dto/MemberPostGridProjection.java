package com.parksupark.soomjae.server.community.post.memberpost.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class MemberPostGridProjection {

    private final Long postId;

    private final String imageUrl;
}
