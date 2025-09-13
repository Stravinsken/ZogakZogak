package com.example.PieceOfPeace.file.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.net.URL;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileService {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    public String uploadFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("업로드할 파일이 없습니다.");
        }

        // 중복을 피하기 위해 UUID를 사용하여 새로운 파일명 생성
        String originalFileName = file.getOriginalFilename();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String storedFileName = "images/" + UUID.randomUUID().toString() + extension; // 'images/' 폴더 안에 저장

        // S3에 업로드할 요청 객체 생성
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(storedFileName)
                .contentType(file.getContentType())
                .build();

        // 파일 업로드
        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        // 업로드된 파일의 공개 URL 반환
        String fileUrl = s3Client.utilities().getUrl(builder -> builder.bucket(bucketName).key(storedFileName)).toExternalForm();

        log.info("File uploaded to S3: {}", fileUrl);

        return fileUrl;
    }

    public void deleteFile(String fileUrl) {
        if (!StringUtils.hasText(fileUrl)) {
            return; // 파일 URL이 없으면 아무것도 하지 않음
        }

        try {
            // URL에서 객체 키(파일 경로) 추출
            URL url = new URL(fileUrl);
            String key = url.getPath().substring(1); // 맨 앞의 '/' 제거

            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
            log.info("File deleted from S3: {}", fileUrl);

        } catch (Exception e) {
            log.error("Failed to delete file from S3: {}", fileUrl, e);
        }
    }
}
