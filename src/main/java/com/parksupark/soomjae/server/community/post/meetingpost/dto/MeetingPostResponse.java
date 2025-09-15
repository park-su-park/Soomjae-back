package com.parksupark.soomjae.server.community.post.meetingpost.dto;

import com.parksupark.soomjae.server.community.post.common.dto.PostResponse;
import com.parksupark.soomjae.server.community.post.meetingpost.entity.MeetingPost;
import lombok.Getter;

@Getter
public class MeetingPostResponse extends MeetingPostBaseResponse implements PostResponse {

    private final Long commentNum;


    private MeetingPostResponse(MeetingPost meetingPost,
        MeetingPostStatsResponse postStatsResponse) {
        super(meetingPost, postStatsResponse.getLikeCount(), postStatsResponse.isLikedByMe(),
            postStatsResponse.getParticipantCount().intValue());
        this.commentNum = postStatsResponse.getPostId();
    }

    public static MeetingPostResponse of(MeetingPost meetingPost,
        MeetingPostStatsResponse postStatsResponse) {
        return new MeetingPostResponse(meetingPost, postStatsResponse);
    }
}
