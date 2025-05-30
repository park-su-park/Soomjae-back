package com.parksupark.soomjae.server.community.location.controller;

import com.parksupark.soomjae.server.community.location.dto.LocationResponseDto;
import com.parksupark.soomjae.server.community.location.service.LocationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @GetMapping("/v1/locations")
    public ResponseEntity<List<LocationResponseDto>> getLocation(
            @RequestParam(required = false) String location) {

        return ResponseEntity.ok(locationService.readByLocation(location));
    }
}
