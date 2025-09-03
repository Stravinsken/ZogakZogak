package com.example.PieceOfPeace.user.controller;

import com.example.PieceOfPeace.jwt.JwtTokenProvider;
import com.example.PieceOfPeace.user.dto.request.FamilyCreateRequest;
import com.example.PieceOfPeace.user.dto.request.LoginRequest;
import com.example.PieceOfPeace.user.dto.request.RegisterRequest;
import com.example.PieceOfPeace.user.dto.respond.LoginRespond;
import com.example.PieceOfPeace.user.entity.User;
import com.example.PieceOfPeace.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider; // JwtTokenProvider 주입

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest request) {
        userService.register(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginRespond> login(@Valid @RequestBody LoginRequest request) {
        // 1. UserService에서 인증된 User 객체 받기
        User user = userService.login(request);

        // 2. JwtTokenProvider를 사용하여 액세스 토큰 생성
        String accessToken = jwtTokenProvider.createAccessToken(user.getEmail(), user.getRole());

        // 3. LoginRespond DTO에 토큰을 담아 반환
        // (리프레시 토큰은 아직 구현되지 않았으므로 null로 설정)
        LoginRespond response = new LoginRespond(accessToken, null);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/family")
    public ResponseEntity<Void> createFamily(@Valid @RequestBody FamilyCreateRequest request, Principal principal) {
        // Principal.getName()은 JwtAuthenticationFilter에서 설정한 사용자의 이메일을 반환합니다.
        String guardianEmail = principal.getName();
        userService.createFamily(request, guardianEmail);
        return ResponseEntity.ok().build();
    }
}
