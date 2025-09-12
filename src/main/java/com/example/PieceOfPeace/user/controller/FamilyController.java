package com.example.PieceOfPeace.user.controller;

import com.example.PieceOfPeace.user.dto.FamilyCreateRequestDto;
import com.example.PieceOfPeace.user.dto.FamilyStatusResponseDto;
import com.example.PieceOfPeace.user.entity.User;
import com.example.PieceOfPeace.user.repository.UserRepository;
import com.example.PieceOfPeace.user.service.FamilyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/family")
@RequiredArgsConstructor
public class FamilyController {

    private final FamilyService familyService;
    private final UserRepository userRepository;

    @GetMapping("/status")
    public ResponseEntity<FamilyStatusResponseDto> checkFamilyStatus(@AuthenticationPrincipal UserDetails userDetails) {

        String email = userDetails.getUsername();
        User guardian = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("인증된 사용자 정보를 찾을 수 없습니다."));
        
        FamilyStatusResponseDto responseDto = familyService.checkFamilyStatus(guardian);
        return ResponseEntity.ok(responseDto);
    }


    @PostMapping
    public ResponseEntity<String> createFamily(@AuthenticationPrincipal UserDetails userDetails, @RequestBody FamilyCreateRequestDto requestDto) {
        String email = userDetails.getUsername();
        User guardian = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("인증된 사용자 정보를 찾을 수 없습니다."));

        familyService.createFamily(guardian, requestDto);
        return ResponseEntity.ok("가족 관계가 성공적으로 등록되었습니다.");
    }
}
