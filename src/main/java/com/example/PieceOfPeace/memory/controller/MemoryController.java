package com.example.PieceOfPeace.memory.controller;

import com.example.PieceOfPeace.memory.dto.request.MemoryCreateRequest;
import com.example.PieceOfPeace.memory.dto.request.MemoryUpdateRequest;
import com.example.PieceOfPeace.memory.dto.response.MemoryResponse;
import com.example.PieceOfPeace.memory.service.MemoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Tag(name = "추억 API", description = "추억(Memory) 관련 CRUD 및 검색 API")
@RestController
@RequestMapping("/api/memories")
@RequiredArgsConstructor
public class MemoryController {

    private final MemoryService memoryService;
    private final ObjectMapper objectMapper; // JSON 변환을 위해 ObjectMapper 주입

    @Operation(summary = "추억 생성 🗺️", description = "새로운 추억을 제목, 내용, 위치 정보, 그리고 미디어 파일과 함께 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "추억 생성 성공"),
            @ApiResponse(responseCode = "400", description = "요청 데이터 유효성 검증 실패"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> createMemory(
            @Parameter(description = "제목, 내용, 위도, 경도를 담은 JSON 데이터")
            @RequestPart("request") @Schema(implementation = MemoryCreateRequest.class) String requestJson,
            @Parameter(description = "업로드할 이미지 또는 음성 파일 목록") @RequestPart(value = "mediaFiles", required = false) List<MultipartFile> mediaFiles,
            Principal principal
    ) throws IOException {
        // 문자열로 받은 JSON을 MemoryCreateRequest 객체로 수동 변환
        MemoryCreateRequest request = objectMapper.readValue(requestJson, MemoryCreateRequest.class);

        String writerEmail = principal.getName();
        memoryService.createMemory(request, mediaFiles, writerEmail);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "추억 단건 조회 🔍", description = "특정 ID를 가진 추억의 상세 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "추억 조회 성공"),
            @ApiResponse(responseCode = "404", description = "해당 ID의 추억을 찾을 수 없음"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
    })
    @GetMapping("/{memoryId}")
    public ResponseEntity<MemoryResponse> findMemoryById(@Parameter(description = "조회할 추억의 ID") @PathVariable Long memoryId) {
        MemoryResponse response = memoryService.findMemoryById(memoryId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "나의 모든 추억 조회 📚", description = "현재 로그인한 사용자가 작성한 모든 추억 목록을 최신순으로 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "추억 목록 조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
    })
    @GetMapping
    public ResponseEntity<List<MemoryResponse>> findMemories(Principal principal) {
        String writerEmail = principal.getName();
        List<MemoryResponse> response = memoryService.findMemoriesByWriter(writerEmail);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "추억 검색 🔎", description = "키워드를 사용하여 추억의 제목, 내용, 작성자 이름에서 일치하는 추억 목록을 검색합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "검색 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
    })
    @GetMapping("/search")
    public ResponseEntity<List<MemoryResponse>> searchMemories(@Parameter(description = "검색할 키워드") @RequestParam String keyword) {
        List<MemoryResponse> results = memoryService.searchMemories(keyword);
        return ResponseEntity.ok(results);
    }

    @Operation(summary = "추억 수정 ✏️", description = "특정 ID를 가진 추억의 제목과 내용을 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "추억 수정 성공"),
            @ApiResponse(responseCode = "400", description = "요청 데이터 유효성 검증 실패"),
            @ApiResponse(responseCode = "403", description = "추억을 수정할 권한이 없음"),
            @ApiResponse(responseCode = "404", description = "해당 ID의 추억을 찾을 수 없음")
    })
    @PatchMapping("/{memoryId}")
    public ResponseEntity<Void> updateMemory(
            @Parameter(description = "수정할 추억의 ID") @PathVariable Long memoryId,
            @Valid @RequestBody MemoryUpdateRequest request,
            Principal principal
    ) {
        String userEmail = principal.getName();
        memoryService.updateMemory(memoryId, request, userEmail);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "추억 삭제 🗑️", description = "특정 ID를 가진 추억을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "추억 삭제 성공"),
            @ApiResponse(responseCode = "403", description = "추억을 삭제할 권한이 없음"),
            @ApiResponse(responseCode = "404", description = "해당 ID의 추억을 찾을 수 없음")
    })
    @DeleteMapping("/{memoryId}")
    public ResponseEntity<Void> deleteMemory(
            @Parameter(description = "삭제할 추억의 ID") @PathVariable Long memoryId,
            Principal principal
    ) {
        String userEmail = principal.getName();
        memoryService.deleteMemory(memoryId, userEmail);
        return ResponseEntity.ok().build();
    }
}
