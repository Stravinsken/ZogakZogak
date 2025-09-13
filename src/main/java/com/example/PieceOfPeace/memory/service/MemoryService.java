package com.example.PieceOfPeace.memory.service;

import com.example.PieceOfPeace.file.service.FileService;
import com.example.PieceOfPeace.memory.dto.request.MemoryCreateRequest;
import com.example.PieceOfPeace.memory.dto.response.MemoryResponse;
import com.example.PieceOfPeace.memory.entity.Memory;
import com.example.PieceOfPeace.memory.repository.MemoryRepository;
import com.example.PieceOfPeace.user.entity.Senior;
import com.example.PieceOfPeace.user.entity.User;
import com.example.PieceOfPeace.user.repository.SeniorRepository;
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
    private final SeniorRepository seniorRepository;
    private final FileService fileService;

    @Transactional
    public MemoryResponse createMemory(Long seniorId, MemoryCreateRequest request, MultipartFile photo) throws IOException {
        // 1. 추억의 주인이 될 어르신 정보를 찾습니다.
        Senior senior = seniorRepository.findById(seniorId)
                .orElseThrow(() -> new IllegalArgumentException("해당 어르신을 찾을 수 없습니다. id=" + seniorId));

        // 2. 사진 파일을 업로드하고 이미지 URL을 받아옵니다.
        String imageUrl = fileService.uploadFile(photo);

        // 3. DTO와 이미지 URL을 바탕으로 Memory 엔티티를 생성합니다.
        Memory memory = Memory.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .imageUrl(imageUrl)
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .memoryDate(request.getMemoryDate())
                .senior(senior)
                .build();

        // 4. 생성된 추억을 DB에 저장합니다.
        Memory savedMemory = memoryRepository.save(memory);

        // 5. 저장된 정보를 바탕으로 Response DTO를 만들어 반환합니다.
        return MemoryResponse.from(savedMemory);
    }

    public List<MemoryResponse> findMemoriesBySenior(Long seniorId) {
        // seniorId로 어르신 존재 여부 확인 (선택적)
        if (!seniorRepository.existsById(seniorId)) {
            throw new IllegalArgumentException("해당 어르신을 찾을 수 없습니다. id=" + seniorId);
        }

        // 특정 어르신의 모든 추억을 날짜 역순으로 조회합니다.
        List<Memory> memories = memoryRepository.findAllBySeniorIdOrderByMemoryDateDesc(seniorId);

        return memories.stream()
                .map(MemoryResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteMemory(User guardian, Long memoryId) {
        // 1. 삭제할 추억을 찾습니다.
        Memory memory = memoryRepository.findById(memoryId)
                .orElseThrow(() -> new IllegalArgumentException("해당 추억을 찾을 수 없습니다. id=" + memoryId));

        // 2. (보안) 요청자가 실제 보호자인지 확인합니다.
        if (!Objects.equals(memory.getSenior().getGuardian().getId(), guardian.getId())) {
            throw new SecurityException("해당 추억을 삭제할 권한이 없습니다.");
        }

        // 3. 서버에 저장된 실제 사진 파일을 삭제합니다.
        fileService.deleteFile(memory.getImageUrl()); // 주석 해제 완료!

        // 4. DB에서 추억 정보를 삭제합니다.
        memoryRepository.delete(memory);
    }
}
