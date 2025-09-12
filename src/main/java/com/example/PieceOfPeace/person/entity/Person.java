package com.example.PieceOfPeace.person.entity;

import com.example.PieceOfPeace.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String vectorId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String relationship;

    private String profileImageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User writer;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Builder
    public Person(String vectorId, String name, String relationship, String profileImageUrl, User writer) {
        this.vectorId = vectorId;
        this.name = name;
        this.relationship = relationship;
        this.profileImageUrl = profileImageUrl;
        this.writer = writer;
    }

    public void update(String name, String relationship) {
        this.name = name;
        this.relationship = relationship;
    }

    public void updateProfileImage(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
