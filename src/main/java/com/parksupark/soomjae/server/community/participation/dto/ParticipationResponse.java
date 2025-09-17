package com.parksupark.soomjae.server.community.participation.dto;

import lombok.Getter;

@Getter
public class ParticipationResponse {

    private final Long postId;
    private final Boolean joined;
    private final Long participantCount;
    private final int capacity;

    public ParticipationResponse(Long postId, Long participantCount, int capacity) {
        this.postId = postId;
        this.joined = true;
        this.participantCount = participantCount;
        this.capacity = capacity;
    }

    public static ParticipationResponse of(Long postId, Long participantCount, int capacity) {
        return new ParticipationResponse(postId, participantCount, capacity);
    }
}
