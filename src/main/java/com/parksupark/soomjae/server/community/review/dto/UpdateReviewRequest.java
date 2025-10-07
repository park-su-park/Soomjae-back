package com.parksupark.soomjae.server.community.review.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.parksupark.soomjae.server.common.constant.ValidationMessages;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import lombok.Getter;

@Getter
public class UpdateReviewRequest {

    @DecimalMin(value = "0.0", message = ValidationMessages.REVIEW_STAR_MIN_VALUE)
    @DecimalMax(value = "5.0", message = ValidationMessages.REVIEW_STAR_MAX_VALUE)
    private final BigDecimal star;

    private final String content;

    @JsonCreator
    public UpdateReviewRequest(
        @JsonProperty("star") BigDecimal star,
        @JsonProperty("content") String content
    ) {
        this.star = star;
        this.content = content;
    }

}
