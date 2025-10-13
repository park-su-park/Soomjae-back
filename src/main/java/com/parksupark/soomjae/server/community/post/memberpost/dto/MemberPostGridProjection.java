package com.parksupark.soomjae.server.community.post.memberpost.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class MemberPostGridProjection {

    private final Long memberPostId;

    private final String image;
}
