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
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest request) {
        userService.register(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginRespond> login(@Valid @RequestBody LoginRequest request) {
        try {
            log.info("로그인 시도: {}", request.email());
            User user = userService.login(request);
            log.info("사용자 인증 성공: {}", user.getEmail());
            
            String accessToken = jwtTokenProvider.createAccessToken(user.getEmail(), user.getRole());
            log.info("JWT 토큰 생성 완료");

            LoginRespond response = new LoginRespond(accessToken, null);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.warn("로그인 실패: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("로그인 중 오류 발생", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/family")
    public ResponseEntity<Void> createFamily(@Valid @RequestBody FamilyCreateRequest request, Principal principal) {
        String guardianEmail = principal.getName();
        userService.createFamily(request, guardianEmail);
        return ResponseEntity.ok().build();
    }
}
