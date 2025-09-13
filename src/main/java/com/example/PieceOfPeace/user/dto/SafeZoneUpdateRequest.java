package com.example.PieceOfPeace.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class SafeZoneUpdateRequest {

    @NotNull
    @Schema(description = "안심구역 중심 위도", example = "37.5665")
    private Double latitude;

    @NotNull
    @Schema(description = "안심구역 중심 경도", example = "126.9780")
    private Double longitude;

    @NotNull
    @Min(100)
    @Max(2000)
    @Schema(description = "안심구역 반경 (미터 단위, 100~2000)", example = "500")
    private Integer radius;
}
