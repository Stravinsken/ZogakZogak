package com.example.PieceOfPeace.diary.dto.response;

import com.example.PieceOfPeace.diary.entity.Diary;
import com.example.PieceOfPeace.diary.entity.Emotion;

import java.time.LocalDateTime;

public record DiaryResponse(
        Long id,
        String content,
        Emotion emotion,
        LocalDateTime createdAt
) {
    // Diary 엔티티를 DiaryResponse DTO로 변환하는 정적 팩토리 메서드
    public static DiaryResponse from(Diary diary) {
        return new DiaryResponse(
                diary.getId(),
                diary.getContent(),
                diary.getEmotion(),
                diary.getCreatedAt()
        );
    }
}
