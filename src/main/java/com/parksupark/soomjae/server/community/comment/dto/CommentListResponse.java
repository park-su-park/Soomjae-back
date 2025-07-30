package com.parksupark.soomjae.server.community.comment.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class CommentListResponse {

    private final List<CommentResponse> comments;

    public CommentListResponse(List<CommentResponse> comments) {
        this.comments = comments;
    }

    public static CommentListResponse of(List<CommentResponse> comments) {
        return new CommentListResponse(comments);
    }
}
