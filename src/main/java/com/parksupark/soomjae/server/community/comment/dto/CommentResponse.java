package com.parksupark.soomjae.server.community.comment.dto;

import com.parksupark.soomjae.server.community.comment.entity.Comment;
import com.parksupark.soomjae.server.member.dto.MemberResponse;
import lombok.Getter;

@Getter
public class CommentResponse {

    private Long commentId;

    private String content;

    private MemberResponse author;

    private CommentResponse(Long commentId, String content, MemberResponse author) {
        this.commentId = commentId;
        this.content = content;
        this.author = author;
    }

    public static CommentResponse of(Comment comment) {
        return new CommentResponse(comment.getId(), comment.getContent(),
                MemberResponse.of(comment.getMember()));
    }
}
