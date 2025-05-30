package com.parksupark.soomjae.server.community.location.service;

import com.parksupark.soomjae.server.community.location.constant.LocationConstant;
import com.parksupark.soomjae.server.community.location.entity.Location;
import com.parksupark.soomjae.server.community.location.repository.LocationRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    public List<String> readByName(String location) {
        //name이 안넘어오면 계층 값이 0인 목록 조회
        if (location == null) {
            return locationRepository.findByHierarchy(0).stream().map(Location::getName)
                    .toList();
        }
        //name이 넘어오면 해당 지역의 하위 지역 조회
        Location parentLocation = locationRepository.findByName(location)
                .orElseThrow(() -> new IllegalStateException(
                        LocationConstant.LOCATION_NOT_FOUND));
        return locationRepository.findByParentCode(parentLocation.getCode()).stream().map(
                Location::getName).toList();
    }
}
