package com.example.PieceOfPeace.memory.service;

import com.example.PieceOfPeace.file.FileSystemStorageService;
import com.example.PieceOfPeace.memory.dto.request.MemoryCreateRequest;
import com.example.PieceOfPeace.memory.dto.request.MemoryUpdateRequest;
import com.example.PieceOfPeace.memory.dto.response.MemoryResponse;
import com.example.PieceOfPeace.memory.entity.Media;
import com.example.PieceOfPeace.memory.entity.MediaType;
import com.example.PieceOfPeace.memory.entity.Memory;
import com.example.PieceOfPeace.memory.repository.MemoryRepository;
import com.example.PieceOfPeace.user.entity.User;
import com.example.PieceOfPeace.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemoryService {

    private final MemoryRepository memoryRepository;
    private final UserRepository userRepository;
    private final FileSystemStorageService fileSystemStorageService; // S3UploadService -> FileSystemStorageService

    @Transactional
    public void createMemory(MemoryCreateRequest request, List<MultipartFile> mediaFiles, String writerEmail) throws IOException {
        User writer = userRepository.findByEmail(writerEmail)
                .orElseThrow(() -> new IllegalArgumentException("작성자 정보를 찾을 수 없습니다."));

        Memory memory = Memory.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .writer(writer)
                .build();

        if (mediaFiles != null && !mediaFiles.isEmpty()) {
            for (MultipartFile file : mediaFiles) {
                // 파일 저장 로직을 새로운 서비스로 변경
                String storedFilePath = fileSystemStorageService.upload(file, "media");
                MediaType mediaType = file.getContentType() != null && file.getContentType().startsWith("image") ? MediaType.IMAGE : MediaType.AUDIO;

                Media media = Media.builder()
                        .mediaUrl(storedFilePath) // DB에는 파일의 상대 경로를 저장
                        .mediaType(mediaType)
                        .build();

                memory.addMedia(media);
            }
        }

        memoryRepository.save(memory);
    }

    public MemoryResponse findMemoryById(Long memoryId) {
        Memory memory = memoryRepository.findById(memoryId)
                .orElseThrow(() -> new IllegalArgumentException("해당 기억을 찾을 수 없습니다."));

        return MemoryResponse.from(memory);
    }

    public List<MemoryResponse> findMemoriesByWriter(String writerEmail) {
        User writer = userRepository.findByEmail(writerEmail)
                .orElseThrow(() -> new IllegalArgumentException("작성자 정보를 찾을 수 없습니다."));

        List<Memory> memories = memoryRepository.findAllByWriterIdOrderByCreatedAtDesc(writer.getId());

        return memories.stream()
                .map(MemoryResponse::from)
                .collect(Collectors.toList());
    }

    public List<MemoryResponse> searchMemories(String keyword) {
        List<Memory> memories = memoryRepository.searchByKeyword(keyword);
        return memories.stream()
                .map(MemoryResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateMemory(Long memoryId, MemoryUpdateRequest request, String userEmail) {
        Memory memory = memoryRepository.findById(memoryId)
                .orElseThrow(() -> new IllegalArgumentException("해당 기억을 찾을 수 없습니다."));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾을 수 없습니다."));

        if (!Objects.equals(memory.getWriter().getId(), user.getId())) {
            throw new SecurityException("기억을 수정할 권한이 없습니다.");
        }

        memory.update(request.title(), request.content());
    }

    @Transactional
    public void deleteMemory(Long memoryId, String userEmail) {
        Memory memory = memoryRepository.findById(memoryId)
                .orElseThrow(() -> new IllegalArgumentException("해당 기억을 찾을 수 없습니다."));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾을 수 없습니다."));

        if (!Objects.equals(memory.getWriter().getId(), user.getId())) {
            throw new SecurityException("기억을 삭제할 권한이 없습니다.");
        }

        // 파일 삭제 로직을 새로운 서비스로 변경
        for (Media media : memory.getMediaList()) {
            fileSystemStorageService.deleteFile(media.getMediaUrl());
        }

        memoryRepository.delete(memory);
    }
}
