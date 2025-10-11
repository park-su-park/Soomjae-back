package com.parksupark.soomjae.server.s3.controller;

import com.parksupark.soomjae.server.s3.service.S3UploaderService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class S3Controller {

    private final S3UploaderService s3UploaderService;

    private static final String DIR_NAME = "static";

    @PostMapping("/images")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<String>> uploadImage(
        @RequestParam("images") List<MultipartFile> multipartFiles) {

        List<String> imageUrls = s3UploaderService.uploadImages(multipartFiles, DIR_NAME);
        return ResponseEntity.ok(imageUrls);
    }

}
