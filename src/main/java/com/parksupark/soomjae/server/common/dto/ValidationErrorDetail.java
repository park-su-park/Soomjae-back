package com.parksupark.soomjae.server.common.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class ValidationErrorDetail {

    private final String field;
    private final Object rejectedValue;
    private final String message;

    @JsonCreator
    public ValidationErrorDetail(
        @JsonProperty String field,
        @JsonProperty Object rejectedValue,
        @JsonProperty String message
    ) {
        this.field = field;
        this.rejectedValue = rejectedValue;
        this.message = message;
    }

}
