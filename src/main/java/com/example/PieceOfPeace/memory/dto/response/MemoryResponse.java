package com.example.PieceOfPeace.memory.dto.response;

import com.example.PieceOfPeace.memory.entity.Memory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Schema(description = "기억 상세 정보 응답 DTO")
public class MemoryResponse {

    @Schema(description = "기억 고유 ID")
    private final Long memoryId;

    @Schema(description = "기억 내용")
    private final String content;

    @Schema(description = "작성자 정보")
    private final WriterResponse writer;

    @Schema(description = "미디어 파일 목록")
    private final List<MediaResponse> mediaList;

    @Schema(description = "생성 일시")
    private final LocalDateTime createdAt;

    @Builder
    public MemoryResponse(Long memoryId, String content, WriterResponse writer, List<MediaResponse> mediaList, LocalDateTime createdAt) {
        this.memoryId = memoryId;
        this.content = content;
        this.writer = writer;
        this.mediaList = mediaList;
        this.createdAt = createdAt;
    }

    // Entity를 DTO로 변환하는 정적 팩토리 메소드
    public static MemoryResponse from(Memory memory) {
        List<MediaResponse> mediaResponses = memory.getMediaList().stream()
                .map(MediaResponse::from)
                .collect(Collectors.toList());

        return MemoryResponse.builder()
                .memoryId(memory.getId())
                .content(memory.getContent())
                .writer(WriterResponse.from(memory.getWriter()))
                .mediaList(mediaResponses)
                .createdAt(memory.getCreatedAt())
                .build();
    }
}
