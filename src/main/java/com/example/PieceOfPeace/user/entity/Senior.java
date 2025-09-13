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

    // 추가적인 어르신 정보 필드 (예: 생년월일, 특이사항 등) 를 여기에 추가할 수 있습니다.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User guardian; // 이 어르신 프로필을 소유한 보호자 계정

    @OneToMany(mappedBy = "senior", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Diary> diaries = new ArrayList<>();

    @Builder
    public Senior(String name, User guardian) {
        this.name = name;
        this.guardian = guardian;
    }
}
