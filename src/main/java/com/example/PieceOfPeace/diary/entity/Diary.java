package com.example.PieceOfPeace.diary.entity;

import com.example.PieceOfPeace.config.AesGcmConverter;
import com.example.PieceOfPeace.user.entity.Senior;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Diary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = AesGcmConverter.class)
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private LocalDate date;

    // Diary의 주인을 User에서 Senior로 변경
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "senior_id", nullable = false)
    private Senior senior;

    @OneToOne(mappedBy = "diary", cascade = CascadeType.ALL, orphanRemoval = true)
    private Emotion emotion;

    @Builder
    public Diary(String content, LocalDate date, Senior senior) { // User -> Senior
        this.content = content;
        this.date = date;
        this.senior = senior;
    }

    public void setEmotion(Emotion emotion) {
        this.emotion = emotion;
    }

    public void update(String content, Double sadness, Double anger, Double fear, Double joy, Double happiness, Double surprise) {
        this.content = content;
        if (this.emotion != null) {
            this.emotion.updateScores(sadness, anger, fear, joy, happiness, surprise);
        }
    }
}
