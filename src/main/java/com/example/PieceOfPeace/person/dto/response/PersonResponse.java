package com.example.PieceOfPeace.person.dto.response;

import com.example.PieceOfPeace.person.entity.Person;

import java.time.LocalDateTime;

public record PersonResponse(
        Long id,
        String vectorId,
        String name,
        String relationship,
        String profileImageUrl,
        LocalDateTime createdAt
) {
    // Person 엔티티를 PersonResponse DTO로 변환하는 정적 팩토리 메서드
    public static PersonResponse from(Person person) {
        return new PersonResponse(
                person.getId(),
                person.getVectorId(),
                person.getName(),
                person.getRelationship(),
                person.getProfileImageUrl(),
                person.getCreatedAt()
        );
    }
}
