package com.parksupark.soomjae.server.common.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;

@Getter
public class ValidationErrorResponse {

    private final String message;
    private final List<ValidationErrorDetail> errors;

    @JsonCreator
    public ValidationErrorResponse(
        @JsonProperty String message,
        @JsonProperty List<ValidationErrorDetail> errors
    ) {
        this.message = message;
        this.errors = errors;
    }

}
