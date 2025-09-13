package com.example.PieceOfPeace.memory.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class MemoryCreateRequest {

    @NotBlank
    @Schema(description = "추억의 제목", example = "가족과 함께한 첫 제주도 여행")
    private String title;

    @Schema(description = "추억에 대한 설명", example = "날씨가 정말 좋았고, 모두가 행복해 보였다.")
    private String description;

    @NotNull
    @Schema(description = "추억이 만들어진 장소의 위도", example = "33.450701")
    private Double latitude;

    @NotNull
    @Schema(description = "추억이 만들어진 장소의 경도", example = "126.570667")
    private Double longitude;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Schema(description = "추억 날짜", example = "2024-05-10", type = "string")
    private LocalDate memoryDate;
}
