package com.parksupark.soomjae.server.community.location.dto;

import com.parksupark.soomjae.server.community.location.entity.Location;
import lombok.Getter;

@Getter
public class LocationResponseDto {

    Long code;
    String name;

    public LocationResponseDto(Long code, String name) {
        this.code = code;
        this.name = name;
    }

    public static LocationResponseDto of(Location location) {
        return new LocationResponseDto(location.getCode(), location.getName());
    }
}
