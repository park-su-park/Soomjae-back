package com.parksupark.soomjae.server.community.review.dto;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReviewResponse {

    private final Long reviewId;

    private final Long participationId;

    private final BigDecimal star;

    private final String content;


}
