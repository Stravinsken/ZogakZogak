package com.example.PieceOfPeace.user.controller;

import com.example.PieceOfPeace.user.dto.SeniorCreateRequestDto;
import com.example.PieceOfPeace.user.dto.SeniorResponseDto;
import com.example.PieceOfPeace.user.entity.User;
import com.example.PieceOfPeace.user.repository.UserRepository;
import com.example.PieceOfPeace.user.service.SeniorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seniors")
@RequiredArgsConstructor
public class SeniorController {

    private final SeniorService seniorService;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<String> createSenior(@AuthenticationPrincipal UserDetails userDetails, @RequestBody SeniorCreateRequestDto requestDto) {
        User guardian = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("인증된 사용자 정보를 찾을 수 없습니다."));

        seniorService.createSenior(guardian, requestDto);
        return ResponseEntity.ok("어르신 프로필이 성공적으로 등록되었습니다.");
    }

    @GetMapping
    public ResponseEntity<List<SeniorResponseDto>> getMySeniors(@AuthenticationPrincipal UserDetails userDetails) {
        User guardian = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("인증된 사용자 정보를 찾을 수 없습니다."));

        List<SeniorResponseDto> seniors = seniorService.findMySeniors(guardian);
        return ResponseEntity.ok(seniors);
    }
}
