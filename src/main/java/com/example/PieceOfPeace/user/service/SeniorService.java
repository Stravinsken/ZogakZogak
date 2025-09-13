package com.example.PieceOfPeace.user.service;

import com.example.PieceOfPeace.user.dto.SeniorCreateRequestDto;
import com.example.PieceOfPeace.user.dto.SeniorResponseDto;
import com.example.PieceOfPeace.user.entity.Senior;
import com.example.PieceOfPeace.user.entity.User;
import com.example.PieceOfPeace.user.repository.SeniorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SeniorService {

    private final SeniorRepository seniorRepository;

    @Transactional
    public void createSenior(User guardian, SeniorCreateRequestDto requestDto) {
        Senior senior = Senior.builder()
                .name(requestDto.getName())
                .guardian(guardian)
                .build();
        seniorRepository.save(senior);
    }

    public List<SeniorResponseDto> findMySeniors(User guardian) {
        List<Senior> seniors = seniorRepository.findAllByGuardianId(guardian.getId());
        return seniors.stream()
                .map(SeniorResponseDto::from)
                .collect(Collectors.toList());
    }
}
