package com.example.PieceOfPeace.user.service;

import com.example.PieceOfPeace.user.dto.FamilyCreateRequestDto;
import com.example.PieceOfPeace.user.dto.FamilyStatusResponseDto;
import com.example.PieceOfPeace.user.entity.Family;
import com.example.PieceOfPeace.user.entity.User;
import com.example.PieceOfPeace.user.repository.FamilyRepository;
import com.example.PieceOfPeace.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FamilyService {

    private final FamilyRepository familyRepository;
    private final UserRepository userRepository;

    public FamilyStatusResponseDto checkFamilyStatus(User guardian) {
        boolean hasFamily = familyRepository.existsByGuardian(guardian);
        return FamilyStatusResponseDto.builder()
                .hasFamily(hasFamily)
                .build();
    }

    @Transactional
    public void createFamily(User guardian, FamilyCreateRequestDto requestDto) {
        User senior = userRepository.findByEmail(requestDto.getSeniorEmail())
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일을 가진 어르신을 찾을 수 없습니다."));

        Family family = Family.builder()
                .guardian(guardian)
                .senior(senior)
                .relationship(requestDto.getRelationship())
                .build();

        familyRepository.save(family);
    }
}
