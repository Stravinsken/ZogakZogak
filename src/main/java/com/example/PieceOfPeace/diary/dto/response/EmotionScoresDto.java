package com.example.PieceOfPeace.diary.dto.response;

import com.example.PieceOfPeace.diary.entity.Emotion;
import lombok.Builder;
import lombok.Getter;

@Getter
public class EmotionScoresDto {
    private final Double sadness;
    private final Double anger;
    private final Double fear;
    private final Double joy;
    private final Double happiness;
    private final Double surprise;

    @Builder
    public EmotionScoresDto(Double sadness, Double anger, Double fear, Double joy, Double happiness, Double surprise) {
        this.sadness = sadness;
        this.anger = anger;
        this.fear = fear;
        this.joy = joy;
        this.happiness = happiness;
        this.surprise = surprise;
    }

    public static EmotionScoresDto from(Emotion emotion) {
        if (emotion == null) {
            return null;
        }
        return EmotionScoresDto.builder()
                .sadness(emotion.getSadness())
                .anger(emotion.getAnger())
                .fear(emotion.getFear())
                .joy(emotion.getJoy())
                .happiness(emotion.getHappiness())
                .surprise(emotion.getSurprise())
                .build();
    }
}
