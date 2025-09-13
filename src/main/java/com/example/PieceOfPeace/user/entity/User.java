package com.example.PieceOfPeace.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users") // "user"는 여러 데이터베이스에서 예약어일 수 있으므로 "users" 사용을 권장합니다.
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String number;

    // 이제 User는 보호자 역할만 하므로, role 필드는 필요 없을 수 있습니다.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @OneToMany(mappedBy = "guardian", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Senior> seniors = new ArrayList<>();

    @Builder
    public User(String email, String password, String name, String number, UserRole role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.number = number;
        this.role = role;
    }
}
