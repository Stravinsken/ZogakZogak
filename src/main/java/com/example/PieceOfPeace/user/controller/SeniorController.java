package com.example.PieceOfPeace.user.controller;

import com.example.PieceOfPeace.user.dto.SafeZoneUpdateRequest;
import com.example.PieceOfPeace.user.dto.SeniorCreateRequestDto;
import com.example.PieceOfPeace.user.dto.SeniorResponseDto;
import com.example.PieceOfPeace.user.entity.User;
import com.example.PieceOfPeace.user.repository.UserRepository;
import com.example.PieceOfPeace.user.service.SeniorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "어르신 관리 API", description = "보호자가 자신의 어르신을 등록, 조회, 관리합니다.")
@RestController
@RequestMapping("/api/seniors")
@RequiredArgsConstructor
public class SeniorController {

    private final SeniorService seniorService;
    private final UserRepository userRepository;

    @Operation(summary = "어르신 프로필 등록", description = "로그인한 보호자가 관리할 어르신의 프로필을 생성합니다.")
    @PostMapping
    public ResponseEntity<String> createSenior(@AuthenticationPrincipal UserDetails userDetails, @RequestBody SeniorCreateRequestDto requestDto) {
        User guardian = getGuardianFromUserDetails(userDetails);
        seniorService.createSenior(guardian, requestDto);
        return ResponseEntity.ok("어르신 프로필이 성공적으로 등록되었습니다.");
    }

    @Operation(summary = "내 어르신 목록 조회", description = "로그인한 보호자에게 등록된 모든 어르신의 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<SeniorResponseDto>> getMySeniors(@AuthenticationPrincipal UserDetails userDetails) {
        User guardian = getGuardianFromUserDetails(userDetails);
        List<SeniorResponseDto> seniors = seniorService.findMySeniors(guardian);
        return ResponseEntity.ok(seniors);
    }

    @Operation(summary = "안심구역 설정/수정", description = "특정 어르신의 안심구역(중심 좌표, 반경)을 설정하거나 수정합니다.")
    @ApiResponse(responseCode = "200", description = "성공적으로 설정됨")
    @ApiResponse(responseCode = "400", description = "요청값이 유효하지 않음 (예: 반경이 100~2000을 벗어남)")
    @ApiResponse(responseCode = "403", description = "해당 어르신에 대한 접근 권한이 없음")
    @PutMapping("/{seniorId}/safe-zone")
    public ResponseEntity<String> updateSafeZone(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long seniorId,
            @Valid @RequestBody SafeZoneUpdateRequest request) {

        User guardian = getGuardianFromUserDetails(userDetails);
        seniorService.updateSafeZone(guardian, seniorId, request);
        return ResponseEntity.ok("안심구역이 성공적으로 설정되었습니다.");
    }

    private User getGuardianFromUserDetails(UserDetails userDetails) {
        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("인증된 사용자 정보를 찾을 수 없습니다."));
    }
}
