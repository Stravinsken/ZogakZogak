package com.example.PieceOfPeace.diary.dto.response;

import com.example.PieceOfPeace.diary.entity.Diary;
import com.example.PieceOfPeace.diary.entity.Emotion;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Getter
public class DiaryResponse {
    private final Long id;
    private final String content;
    private final LocalDate date;
    private final String emotion;
    private final EmotionScoresDto emotionScores;

    @Builder
    public DiaryResponse(Long id, String content, LocalDate date, String emotion, EmotionScoresDto emotionScores) { // contents -> content
        this.id = id;
        this.content = content;
        this.date = date;
        this.emotion = emotion;
        this.emotionScores = emotionScores;
    }

    public static DiaryResponse from(Diary diary) {
        Emotion emotionEntity = diary.getEmotion();
        String dominantEmotion = findDominantEmotion(emotionEntity);

        return DiaryResponse.builder()
                .id(diary.getId())
                .content(diary.getContent())
                .date(diary.getDate())
                .emotion(dominantEmotion)
                .emotionScores(EmotionScoresDto.from(emotionEntity))
                .build();
    }

    private static String findDominantEmotion(Emotion emotion) {
        if (emotion == null) {
            return "neutral";
        }

        Map<String, Double> scores = new HashMap<>();
        scores.put("sadness", emotion.getSadness());
        scores.put("anger", emotion.getAnger());
        scores.put("fear", emotion.getFear());
        scores.put("joy", emotion.getJoy());
        scores.put("happiness", emotion.getHappiness());
        scores.put("surprise", emotion.getSurprise());

        if (scores.values().stream().allMatch(Objects::isNull)) {
            return "neutral";
        }

        return scores.entrySet().stream()
                .filter(entry -> entry.getValue() != null)
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("neutral");
    }
}
