package com.example.PieceOfPeace.memory.entity;

import com.example.PieceOfPeace.user.entity.Senior;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Memory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description; // content -> description으로 필드명 변경

    @Column(nullable = false)
    private String imageUrl; // 사진 URL 필드 추가

    @Column
    private Double latitude;

    @Column
    private Double longitude;

    @Column(nullable = false)
    private LocalDate memoryDate; // 추억 날짜 필드 추가

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "senior_id", nullable = false) // writer -> senior로 변경
    private Senior senior;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public Memory(String title, String description, String imageUrl, Double latitude, Double longitude, LocalDate memoryDate, Senior senior) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.latitude = latitude;
        this.longitude = longitude;
        this.memoryDate = memoryDate;
        this.senior = senior;
    }

    public void update(String newTitle, String newDescription) {
        this.title = newTitle;
        this.description = newDescription;
    }
}
