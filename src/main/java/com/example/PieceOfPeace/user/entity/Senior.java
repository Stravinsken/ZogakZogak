package com.example.PieceOfPeace.user.entity;

import com.example.PieceOfPeace.diary.entity.Diary;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Senior {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User guardian;

    @OneToMany(mappedBy = "senior", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Diary> diaries = new ArrayList<>();

    private Double safeZoneLatitude;
    private Double safeZoneLongitude;
    private Integer safeZoneRadius;

    @Builder
    public Senior(String name, User guardian) {
        this.name = name;
        this.guardian = guardian;
    }

    public void updateSafeZone(Double latitude, Double longitude, Integer radius) {
        this.safeZoneLatitude = latitude;
        this.safeZoneLongitude = longitude;
        this.safeZoneRadius = radius;
    }
}
