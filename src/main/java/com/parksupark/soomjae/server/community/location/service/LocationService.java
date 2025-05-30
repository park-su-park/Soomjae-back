package com.parksupark.soomjae.server.community.location.service;

import com.parksupark.soomjae.server.community.location.dto.LocationResponseDto;
import com.parksupark.soomjae.server.community.location.repository.LocationRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    public List<LocationResponseDto> readByLocation(String location) {
        if (location == null) {
            return locationRepository.findByHierarchy(0).stream().map(LocationResponseDto::of)
                    .toList();
        }
        return locationRepository.findByParentCode(Long.parseLong(location)).stream()
                .map(LocationResponseDto::of)
                .toList();
    }
}
