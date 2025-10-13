package com.parksupark.soomjae.server.community.post.memberpost.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.Getter;

@Getter
public class SaveMemberPostRequest {

    @NotBlank
    private final String content;
    private final List<String> imageUrls;

    @JsonCreator
    public SaveMemberPostRequest(
        @JsonProperty("content") String content,
        @JsonProperty("imageUrls") List<String> imageUrls
    ) {
        this.content = content;
        this.imageUrls = imageUrls;
    }
}
