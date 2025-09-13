package com.example.PieceOfPeace.diary.controller;

import com.example.PieceOfPeace.diary.dto.request.DiaryCreateRequest;
import com.example.PieceOfPeace.diary.dto.request.DiaryUpdateRequest;
import com.example.PieceOfPeace.diary.dto.response.DiaryResponse;
import com.example.PieceOfPeace.diary.service.DiaryService;
import com.example.PieceOfPeace.user.entity.User;
import com.example.PieceOfPeace.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/diaries")
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;
    private final UserRepository userRepository;

    // 새로운 일기 생성 API
    @PostMapping
    public ResponseEntity<String> createDiary(@RequestBody DiaryCreateRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        User guardian = findGuardianByEmail(userDetails.getUsername());
        diaryService.createDiary(request, guardian);
        return ResponseEntity.ok("일기가 성공적으로 작성되었습니다.");
    }

    // 특정 어르신의 모든 일기 목록 조회 API
    @GetMapping("/senior/{seniorId}")
    public ResponseEntity<List<DiaryResponse>> getDiariesBySenior(@PathVariable Long seniorId, @AuthenticationPrincipal UserDetails userDetails) {
        User guardian = findGuardianByEmail(userDetails.getUsername());
        List<DiaryResponse> diaries = diaryService.findDiariesBySenior(seniorId, guardian);
        return ResponseEntity.ok(diaries);
    }

    // 일기 수정 API
    @PatchMapping("/{diaryId}")
    public ResponseEntity<String> updateDiary(@PathVariable Long diaryId, @RequestBody DiaryUpdateRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        User guardian = findGuardianByEmail(userDetails.getUsername());
        diaryService.updateDiary(diaryId, request, guardian);
        return ResponseEntity.ok("일기가 성공적으로 수정되었습니다.");
    }

    // 일기 삭제 API
    @DeleteMapping("/{diaryId}")
    public ResponseEntity<Void> deleteDiary(@PathVariable Long diaryId, @AuthenticationPrincipal UserDetails userDetails) {
        User guardian = findGuardianByEmail(userDetails.getUsername());
        diaryService.deleteDiary(diaryId, guardian);
        return ResponseEntity.noContent().build();
    }

    private User findGuardianByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("인증된 사용자 정보를 찾을 수 없습니다."));
    }
}
