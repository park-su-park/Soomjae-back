package com.parksupark.soomjae.server.s3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.parksupark.soomjae.server.common.exception.ErrorMessages;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3UploaderService {

    private final AmazonS3 amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private static final String TMP_DIR = "java.io.tmpdir";

    public List<String> uploadImages(List<MultipartFile> multipartFiles, String dirName) {
        List<String> imageUrls = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            try{
                String imageUrl = upload(multipartFile, dirName);
                imageUrls.add(imageUrl);
            } catch (IOException e) {
                throw new IllegalArgumentException(ErrorMessages.MULTIPART_FILE_CONVERT_ERROR);
            }
        }
        return imageUrls;
    }

    // MultipartFile을 전달받아 File로 전환한 후 S3에 업로드
    private String upload(MultipartFile multipartFile, String dirName) throws IOException {
        File uploadFile = convert(multipartFile)
            .orElseThrow(() -> new IllegalArgumentException(ErrorMessages.MULTIPART_FILE_CONVERT_ERROR));
        return upload(uploadFile, dirName);
    }

    private String upload(File uploadFile, String dirName) {
        String fileName = dirName + "/" + UUID.randomUUID() + "_" + uploadFile.getName();
        String uploadImageUrl = putS3(uploadFile, fileName);

        removeNewFile(uploadFile);  // 로컬에 생성된 File 삭제

        return uploadImageUrl;      // 업로드된 파일의 S3 URL 주소 반환
    }

    // S3에 파일을 업로드하고 URL을 반환하는 메서드
    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(
            new PutObjectRequest(bucket, fileName, uploadFile)
                .withCannedAcl(CannedAccessControlList.PublicRead)	// PublicRead 권한으로 업로드
        );
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    // 로컬에 저장된 임시 파일을 삭제하는 메서드
    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        } else {
            log.info("파일이 삭제되지 못했습니다.");
        }
    }

    // MultipartFile을 File 객체로 변환하는 메서드
    private Optional<File> convert(MultipartFile file) throws IOException {
        // 임시 파일 경로를 사용하므로 파일 이름이 중복되어도 안전합니다.
        File convertFile = new File(System.getProperty(TMP_DIR) + "/" + file.getOriginalFilename());

        // 동일한 이름의 파일이 존재하는 경우, 덮어쓰기 위해 바로 FileOutputStream을 사용합니다.
        try (FileOutputStream fos = new FileOutputStream(convertFile)) {
            fos.write(file.getBytes());
        }
        return Optional.of(convertFile);
    }
}
