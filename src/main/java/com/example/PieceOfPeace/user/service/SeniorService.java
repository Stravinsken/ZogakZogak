package com.example.PieceOfPeace.user.service;

import com.example.PieceOfPeace.user.dto.SafeZoneUpdateRequest;
import com.example.PieceOfPeace.user.dto.SeniorCreateRequestDto;
import com.example.PieceOfPeace.user.dto.SeniorResponseDto;
import com.example.PieceOfPeace.user.entity.Senior;
import com.example.PieceOfPeace.user.entity.User;
import com.example.PieceOfPeace.user.repository.SeniorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
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

    @Transactional
    public void updateSafeZone(User guardian, Long seniorId, SafeZoneUpdateRequest request) {
        Senior senior = seniorRepository.findById(seniorId)
                .orElseThrow(() -> new IllegalArgumentException("해당 어르신을 찾을 수 없습니다. id=" + seniorId));

        if (!Objects.equals(senior.getGuardian().getId(), guardian.getId())) {
            throw new SecurityException("해당 어르신에 대한 접근 권한이 없습니다.");
        }

        senior.updateSafeZone(
                request.getLatitude(),
                request.getLongitude(),
                request.getRadius()
        );
    }
}
