package com.example.PieceOfPeace.memory.dto.response;

import com.example.PieceOfPeace.memory.entity.Media;
import com.example.PieceOfPeace.memory.entity.MediaType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Schema(description = "미디어 파일 응답 DTO")
public class MediaResponse {

    @Schema(description = "미디어 파일 주소(URL)")
    private final String mediaUrl;

    @Schema(description = "미디어 파일 종류 (IMAGE 또는 AUDIO)")
    private final MediaType mediaType;

    @Builder
    public MediaResponse(String mediaUrl, MediaType mediaType) {
        this.mediaUrl = mediaUrl;
        this.mediaType = mediaType;
    }

    // Entity를 DTO로 변환하는 정적 팩토리 메소드
    public static MediaResponse from(Media media) {
        return MediaResponse.builder()
                .mediaUrl(media.getMediaUrl())
                .mediaType(media.getMediaType())
                .build();
    }
}
