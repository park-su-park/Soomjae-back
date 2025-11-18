package com.parksupark.soomjae.server.profile.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CheckDuplicateNicknameResponse {

    private final boolean duplicate;

}
