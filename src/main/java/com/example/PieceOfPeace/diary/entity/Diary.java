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
    private String content;

    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne(mappedBy = "diary", cascade = CascadeType.ALL, orphanRemoval = true)
    private Emotion emotion;

    @Builder
    public Diary(String contents, LocalDate date, User user) {
        this.content = contents;
        this.date = date;
        this.user = user;
    }

    public void setEmotion(Emotion emotion) {
        this.emotion = emotion;
    }

    public void update(String contents, Emotion emotion) {
        this.content = contents;
        this.emotion.updateScores(emotion.getSadness(), emotion.getAnger(), emotion.getFear(), emotion.getJoy(), emotion.getHappiness(), emotion.getSurprise());
    }
}
