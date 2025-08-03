package com.parksupark.soomjae.server.community.post.meetingpost.dto;

import com.parksupark.soomjae.server.community.comment.dto.CommentResponse;
import com.parksupark.soomjae.server.community.post.meetingpost.entity.MeetingPost;
import java.util.List;
import lombok.Getter;

@Getter
public class MeetingPostDetailResponse extends MeetingPostBaseResponse {

    private final List<CommentResponse> comments;

    private MeetingPostDetailResponse(MeetingPost meetingPost, Long likeNum,
        Boolean isLikedByMe, List<CommentResponse> comments) {
        super(meetingPost, likeNum, isLikedByMe);
        this.comments = comments;
    }

    public static MeetingPostDetailResponse of(MeetingPost meetingPost, Long likeNum,
        Boolean isLikedByMe, List<CommentResponse> comments) {
        return new MeetingPostDetailResponse(meetingPost, likeNum, isLikedByMe, comments);
    }
}