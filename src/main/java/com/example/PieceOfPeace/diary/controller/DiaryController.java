package com.example.PieceOfPeace.diary.controller;

import com.example.PieceOfPeace.diary.dto.request.DiaryCreateRequest;
import com.example.PieceOfPeace.diary.dto.request.DiaryUpdateRequest;
import com.example.PieceOfPeace.diary.dto.response.DiaryResponse;
import com.example.PieceOfPeace.diary.service.DiaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Tag(name = "감정 일기 API", description = "감정 일기(Diary) 관련 CRUD API")
@RestController
@RequestMapping("/api/diaries")
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;

    @Operation(summary = "감정 일기 생성 ✍️", description = "새로운 일기를 작성하면, 내용 기반으로 감정을 자동 분석하여 함께 저장합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "일기 생성 성공"),
            @ApiResponse(responseCode = "400", description = "요청 데이터 유효성 검증 실패"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
    })
    @PostMapping
    public ResponseEntity<Void> createDiary(@Valid @RequestBody DiaryCreateRequest request, Principal principal) {
        String writerEmail = principal.getName();
        diaryService.createDiary(request, writerEmail);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "나의 모든 감정 일기 조회 📖", description = "현재 로그인한 사용자가 작성한 모든 일기 목록을 최신순으로 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "일기 목록 조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
    })
    @GetMapping
    public ResponseEntity<List<DiaryResponse>> findMyDiaries(Principal principal) {
        String writerEmail = principal.getName();
        List<DiaryResponse> response = diaryService.findMyDiaries(writerEmail);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "감정 일기 수정 ✏️", description = "특정 ID를 가진 일기의 내용을 수정합니다. 수정 시 감정은 다시 분석됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "일기 수정 성공"),
            @ApiResponse(responseCode = "400", description = "요청 데이터 유효성 검증 실패"),
            @ApiResponse(responseCode = "403", description = "일기를 수정할 권한이 없음"),
            @ApiResponse(responseCode = "404", description = "해당 ID의 일기를 찾을 수 없음")
    })
    @PatchMapping("/{diaryId}")
    public ResponseEntity<Void> updateDiary(
            @Parameter(description = "수정할 일기의 ID") @PathVariable Long diaryId,
            @Valid @RequestBody DiaryUpdateRequest request,
            Principal principal
    ) {
        String userEmail = principal.getName();
        diaryService.updateDiary(diaryId, request, userEmail);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "감정 일기 삭제 🗑️", description = "특정 ID를 가진 일기를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "일기 삭제 성공"),
            @ApiResponse(responseCode = "403", description = "일기를 삭제할 권한이 없음"),
            @ApiResponse(responseCode = "404", description = "해당 ID의 일기를 찾을 수 없음")
    })
    @DeleteMapping("/{diaryId}")
    public ResponseEntity<Void> deleteDiary(
            @Parameter(description = "삭제할 일기의 ID") @PathVariable Long diaryId,
            Principal principal
    ) {
        String userEmail = principal.getName();
        diaryService.deleteDiary(diaryId, userEmail);
        return ResponseEntity.ok().build();
    }
}
