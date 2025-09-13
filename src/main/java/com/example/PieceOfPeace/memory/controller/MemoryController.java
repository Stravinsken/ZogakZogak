package com.example.PieceOfPeace.memory.controller;

import com.example.PieceOfPeace.memory.dto.request.MemoryCreateRequest;
import com.example.PieceOfPeace.memory.dto.response.MemoryResponse;
import com.example.PieceOfPeace.memory.service.MemoryService;
import com.example.PieceOfPeace.user.entity.User;
import com.example.PieceOfPeace.user.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Tag(name = "추억 API", description = "어르신의 추억(Memory) 관련 API")
@RestController
@RequiredArgsConstructor
public class MemoryController {

    private final MemoryService memoryService;
    private final UserRepository userRepository;

    @Operation(summary = "추억 생성", description = "특정 어르신에게 사진과 함께 새로운 추억을 등록합니다.")
    @PostMapping(value = "/api/seniors/{seniorId}/memories", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MemoryResponse> createMemory(
            @Parameter(description = "추억을 등록할 어르신의 ID") @PathVariable Long seniorId,
            @Parameter(description = "제목, 설명, 날짜 등 텍스트 정보 (JSON 형식)") @RequestPart("request") @Valid MemoryCreateRequest request,
            @Parameter(description = "업로드할 사진 파일 (1개)") @RequestPart("photo") MultipartFile photo
    ) throws IOException {
        MemoryResponse memoryResponse = memoryService.createMemory(seniorId, request, photo);
        return ResponseEntity.status(HttpStatus.CREATED).body(memoryResponse);
    }

    @Operation(summary = "특정 어르신의 추억 목록 조회", description = "특정 어르신에게 등록된 모든 추억 목록을 날짜순으로 조회합니다.")
    @GetMapping("/api/seniors/{seniorId}/memories")
    public ResponseEntity<List<MemoryResponse>> findMemoriesBySenior(
            @Parameter(description = "추억 목록을 조회할 어르신의 ID") @PathVariable Long seniorId
    ) {
        List<MemoryResponse> memories = memoryService.findMemoriesBySenior(seniorId);
        return ResponseEntity.ok(memories);
    }

    @Operation(summary = "추억 삭제", description = "특정 ID를 가진 추억을 삭제합니다. 해당 어르신의 보호자만 삭제할 수 있습니다.")
    @DeleteMapping("/api/memories/{memoryId}")
    public ResponseEntity<Void> deleteMemory(
            @Parameter(description = "삭제할 추억의 ID") @PathVariable Long memoryId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        User guardian = getGuardianFromUserDetails(userDetails);
        memoryService.deleteMemory(guardian, memoryId);
        return ResponseEntity.ok().build();
    }

    private User getGuardianFromUserDetails(UserDetails userDetails) {
        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("인증된 사용자 정보를 찾을 수 없습니다."));
    }
}
