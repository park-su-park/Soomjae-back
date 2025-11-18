package com.parksupark.soomjae.server.community.post.meetingpost.dto;

public class ParticipationCreatedEvent {

    private final Long participationId;

    public ParticipationCreatedEvent(Long participationId) {
        this.participationId = participationId;
    }

    public Long getParticipationId() {
        return participationId;
    }

}
