package com.parksupark.soomjae.server.fcm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlarmDto {

    private String title;

    private String content;

    private String img;

    private String url;
}
