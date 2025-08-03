package com.parksupark.soomjae.server.community.post.meetingpost.dto;

import com.parksupark.soomjae.server.community.post.common.dto.PostResponse;
import com.parksupark.soomjae.server.community.post.meetingpost.entity.MeetingPost;
import lombok.Getter;

@Getter
public class MeetingPostResponse extends MeetingPostBaseResponse implements PostResponse {

    private final Long commentNum;

    private MeetingPostResponse(MeetingPost meetingPost, Long likeNum, Boolean isLikedByMe,
        Long commentNum) {
        super(meetingPost, likeNum, isLikedByMe);
        this.commentNum = commentNum;
    }

    public static MeetingPostResponse of(MeetingPost meetingPost, Long likeNum,
        Boolean isLikedByMe, Long commentNum) {
        return new MeetingPostResponse(meetingPost, likeNum, isLikedByMe, commentNum);
    }
}
