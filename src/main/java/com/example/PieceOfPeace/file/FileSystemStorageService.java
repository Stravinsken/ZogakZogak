package com.example.PieceOfPeace.file;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileSystemStorageService {

    // application.properties에 설정할 업로드 경로
    @Value("${file.upload-dir}")
    private String uploadDir;

    // 서비스가 시작될 때 업로드 폴더를 생성
    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(Paths.get(uploadDir));
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage", e);
        }
    }

    // 파일 업로드 처리
    public String upload(MultipartFile file, String dirName) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Failed to store empty file.");
        }

        // 'uploads/media' 와 같은 하위 디렉토리 생성
        Path directoryPath = Paths.get(uploadDir, dirName);
        Files.createDirectories(directoryPath);

        // 파일 이름이 겹치지 않도록 UUID 사용
        String originalFilename = file.getOriginalFilename();
        String storedFileName = UUID.randomUUID() + "_" + originalFilename;
        Path destinationFile = directoryPath.resolve(storedFileName).normalize().toAbsolutePath();

        // 파일을 목적지로 복사
        file.transferTo(destinationFile);

        // DB에 저장할 상대 경로 반환 (예: "media/uuid_image.jpg")
        return Paths.get(dirName, storedFileName).toString();
    }

    // 파일 삭제 처리
    public void deleteFile(String filePath) {
        if (filePath == null || filePath.isBlank()) {
            return;
        }
        try {
            Path fileToDelete = Paths.get(uploadDir).resolve(filePath).normalize().toAbsolutePath();
            Files.deleteIfExists(fileToDelete);
        } catch (IOException e) {
            // 로깅 추가 권장
            System.err.println("Failed to delete file: " + filePath);
        }
    }
}
