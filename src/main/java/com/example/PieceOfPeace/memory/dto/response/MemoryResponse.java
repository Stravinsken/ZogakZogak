package com.example.PieceOfPeace.memory.dto.response;

import com.example.PieceOfPeace.memory.entity.Memory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class MemoryResponse {

    @Schema(description = "추억 고유 ID", example = "1")
    private final Long memoryId;

    @Schema(description = "추억 제목", example = "가족과 함께한 첫 제주도 여행")
    private final String title;

    @Schema(description = "추억 설명", example = "날씨가 정말 좋았고, 모두가 행복해 보였다.")
    private final String description;

    @Schema(description = "사진 이미지 URL", example = "https://example.s3.amazonaws.com/images/photo.jpg")
    private final String imageUrl;

    @Schema(description = "장소 위도", example = "33.450701")
    private final Double latitude;

    @Schema(description = "장소 경도", example = "126.570667")
    private final Double longitude;

    @Schema(description = "추억 날짜", example = "2024-05-10")
    private final LocalDate memoryDate;

    @Schema(description = "생성 일시")
    private final LocalDateTime createdAt;

    @Builder
    public MemoryResponse(Long memoryId, String title, String description, String imageUrl, Double latitude, Double longitude, LocalDate memoryDate, LocalDateTime createdAt) {
        this.memoryId = memoryId;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.latitude = latitude;
        this.longitude = longitude;
        this.memoryDate = memoryDate;
        this.createdAt = createdAt;
    }

    public static MemoryResponse from(Memory memory) {
        return MemoryResponse.builder()
                .memoryId(memory.getId())
                .title(memory.getTitle())
                .description(memory.getDescription())
                .imageUrl(memory.getImageUrl())
                .latitude(memory.getLatitude())
                .longitude(memory.getLongitude())
                .memoryDate(memory.getMemoryDate())
                .createdAt(memory.getCreatedAt())
                .build();
    }
}
