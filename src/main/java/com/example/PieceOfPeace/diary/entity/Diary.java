package com.example.PieceOfPeace.diary.entity;

import com.example.PieceOfPeace.user.entity.User;
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

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content; // contents -> content

    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne(mappedBy = "diary", cascade = CascadeType.ALL, orphanRemoval = true)
    private Emotion emotion;

    @Builder
    public Diary(String content, LocalDate date, User user) { // contents -> content
        this.content = content;
        this.date = date;
        this.user = user;
    }

    public void setEmotion(Emotion emotion) {
        this.emotion = emotion;
    }

    public void update(String content, Double sadness, Double anger, Double fear, Double joy, Double happiness, Double surprise) { // contents -> content
        this.content = content;
        if (this.emotion != null) {
            this.emotion.updateScores(sadness, anger, fear, joy, happiness, surprise);
        }
    }
}
