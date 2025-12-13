package com.example.PieceOfPeace.diary.entity;

import com.example.PieceOfPeace.config.AesGcmConverter;
import com.example.PieceOfPeace.user.entity.Senior;
import jakarta.persistence.*;
import lombok.*;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "senior_id", nullable = false)
    private Senior senior;

    @Setter
    @OneToOne(mappedBy = "diary", cascade = CascadeType.ALL, orphanRemoval = true)
    private Emotion emotion;

    @Builder
    public Diary(String content, LocalDate date, Senior senior) { // User -> Senior
        this.content = content;
        this.date = date;
        this.senior = senior;
    }

    public void update(String content, Double sadness, Double anger, Double fear, Double joy, Double happiness, Double surprise) {
        this.content = content;
        if (this.emotion != null) {
            this.emotion.updateScores(sadness, anger, fear, joy, happiness, surprise);
        }
    }
}
