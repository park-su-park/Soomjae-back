package com.parksupark.soomjae.server.community.review.dto;

import javax.annotation.Nullable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReviewExistenceResponse {

    private final boolean existence;

    @Nullable
    private final Long reviewId;

}
