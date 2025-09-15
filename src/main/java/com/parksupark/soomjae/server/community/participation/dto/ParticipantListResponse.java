package com.parksupark.soomjae.server.community.participation.dto;

import com.parksupark.soomjae.server.member.dto.MemberResponse;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ParticipantListResponse {

    List<MemberResponse> participants;

    public static ParticipantListResponse of(List<MemberResponse> participants) {
        return new ParticipantListResponse(participants);
    }
}
