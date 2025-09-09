package com.example.PieceOfPeace.medication.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "약 알림 생성 요청 DTO")
public record MedicationCreateRequest(
        @Schema(description = "약 이름", example = "혈압약")
        @NotBlank
        String pillName,

        @Schema(description = "알림 시간 (HH:mm 형식)", example = "09:00")
        @NotBlank
        @Pattern(regexp = "^([01]\\d|2[0-3]):([0-5]\\d)$", message = "알림 시간은 HH:mm 형식이어야 합니다.")
        String notificationTime
) {
}
