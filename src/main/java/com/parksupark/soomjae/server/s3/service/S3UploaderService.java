package com.parksupark.soomjae.server.s3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.parksupark.soomjae.server.common.exception.ErrorMessages;
import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3UploaderService {

    private final AmazonS3 amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private Path tempDir; // 애플리케이션 전용 임시 디렉터리 경로

    @PostConstruct
    public void init() throws IOException {
        // 1. 파일 권한 설정: 소유자만 읽기, 쓰기, 실행 가능 (rwx------)
//        Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rwx------");
//        FileAttribute<Set<PosixFilePermission>> attr = PosixFilePermissions.asFileAttribute(perms);

        // 2. 위에서 정의한 권한으로 전용 임시 디렉터리를 생성합니다.
        //    이제 이 디렉터리는 생성 시점부터 다른 사용자의 접근이 원천적으로 차단됩니다.
        this.tempDir = Files.createTempDirectory("soomjae-s3-temp-"); //, attr
    }

    public List<String> uploadImages(List<MultipartFile> multipartFiles, String dirName) {
        List<String> imageUrls = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            try {
                String imageUrl = upload(multipartFile, dirName);
                imageUrls.add(imageUrl);
            } catch (IOException e) {
                throw new IllegalArgumentException(ErrorMessages.MULTIPART_FILE_CONVERT_ERROR);
            }
        }
        return imageUrls;
    }

    private String upload(MultipartFile multipartFile, String dirName) throws IOException {
        // 1. 안전한 임시 파일 생성 (사용자 파일 이름 사용 안 함)
        File uploadFile = convert(multipartFile)
            .orElseThrow(
                () -> new IllegalArgumentException(ErrorMessages.MULTIPART_FILE_CONVERT_ERROR));

        // 2. S3에 저장할 파일 이름 생성 (여기서는 원본 파일 이름 사용)
        String originalFileName = multipartFile.getOriginalFilename();
        String uniqueFileName = dirName + "/" + UUID.randomUUID() + "_" + originalFileName;

        // 3. S3에 업로드
        String uploadImageUrl = putS3(uploadFile, uniqueFileName);

        // 4. 로컬 임시 파일 삭제
        removeNewFile(uploadFile);

        return uploadImageUrl;
    }

    // S3에 파일을 업로드하고 URL을 반환하는 메서드
    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(
            new PutObjectRequest(bucket, fileName, uploadFile));

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

    private Optional<File> convert(MultipartFile file) throws IOException {
        // 1. 애플리케이션 전용 임시 디렉터리 안에 안전한 임시 파일 생성
        Path tempFilePath = Files.createTempFile(this.tempDir, "upload_", ".tmp");

        // 2. MultipartFile의 내용을 임시 파일에 씁니다.
        file.transferTo(tempFilePath);

        // 3. File 객체로 변환하여 반환
        return Optional.of(tempFilePath.toFile());
    }
}
