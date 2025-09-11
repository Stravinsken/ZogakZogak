package com.example.PieceOfPeace.medication.controller;

import com.example.PieceOfPeace.medication.dto.request.MedicationCreateRequest;
import com.example.PieceOfPeace.medication.dto.response.MedicationResponse;
import com.example.PieceOfPeace.medication.service.MedicationService;
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

@Tag(name = "약 알림 API", description = "약 복용 알림 관련 CRUD API")
@RestController
@RequestMapping("/api/medications")
@RequiredArgsConstructor
public class MedicationController {

    private final MedicationService medicationService;

    @Operation(summary = "약 알림 생성 ⏰", description = "새로운 약 복용 알림을 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "약 알림 생성 성공"),
            @ApiResponse(responseCode = "400", description = "요청 데이터 유효성 검증 실패"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
    })
    @PostMapping
    public ResponseEntity<Void> createMedication(@Valid @RequestBody MedicationCreateRequest request, Principal principal) {
        medicationService.createMedication(request, principal.getName());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "나의 약 알림 목록 조회 📋", description = "현재 로그인한 사용자의 모든 약 알림 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "약 알림 목록 조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
    })
    @GetMapping
    public ResponseEntity<List<MedicationResponse>> findMyMedications(Principal principal) {
        List<MedicationResponse> response = medicationService.findMyMedications(principal.getName());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "약 복용 완료 처리 ✅", description = "특정 약 알림을 복용 완료 상태로 변경합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "복용 완료 처리 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @ApiResponse(responseCode = "403", description = "해당 약 알림에 대한 권한이 없음"),
            @ApiResponse(responseCode = "404", description = "해당 ID의 약 알림을 찾을 수 없음")
    })
    @PatchMapping("/{medicationId}/take")
    public ResponseEntity<Void> markMedicationAsTaken(@Parameter(description = "복용 완료 처리할 약 알림의 ID") @PathVariable Long medicationId, Principal principal) {
        medicationService.markMedicationAsTaken(medicationId, principal.getName());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "약 알림 삭제 ❌", description = "특정 약 알림을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "약 알림 삭제 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @ApiResponse(responseCode = "403", description = "해당 약 알림에 대한 권한이 없음"),
            @ApiResponse(responseCode = "404", description = "해당 ID의 약 알림을 찾을 수 없음")
    })
    @DeleteMapping("/{medicationId}")
    public ResponseEntity<Void> deleteMedication(@Parameter(description = "삭제할 약 알림의 ID") @PathVariable Long medicationId, Principal principal) {
        medicationService.deleteMedication(medicationId, principal.getName());
        return ResponseEntity.ok().build();
    }
}
