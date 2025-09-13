package com.example.PieceOfPeace.diary.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Emotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double sadness;
    private Double anger;
    private Double fear;
    private Double joy;
    private Double happiness;
    private Double surprise;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_id", nullable = false)
    private Diary diary;

    @Builder
    public Emotion(Double sadness, Double anger, Double fear, Double joy, Double happiness, Double surprise, Diary diary) {
        this.sadness = sadness;
        this.anger = anger;
        this.fear = fear;
        this.joy = joy;
        this.happiness = happiness;
        this.surprise = surprise;
        this.diary = diary;
    }

    public void updateScores(Double sadness, Double anger, Double fear, Double joy, Double happiness, Double surprise) {
        this.sadness = sadness;
        this.anger = anger;
        this.fear = fear;
        this.joy = joy;
        this.happiness = happiness;
        this.surprise = surprise;
    }
}
