package com.example.PieceOfPeace.user.controller;

import com.example.PieceOfPeace.jwt.JwtTokenProvider;
import com.example.PieceOfPeace.jwt.entity.RefreshToken;
import com.example.PieceOfPeace.jwt.service.RefreshTokenService;
import com.example.PieceOfPeace.user.dto.request.TokenRefreshRequest;
import com.example.PieceOfPeace.user.dto.response.TokenRefreshResponse;
import com.example.PieceOfPeace.user.entity.User;
import com.example.PieceOfPeace.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RefreshTokenService refreshTokenService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.refreshToken();

        // 1. 리프레시 토큰 유효성 검증 (JWT 자체의 유효성)
        if (!jwtTokenProvider.validateToken(requestRefreshToken) || !"refresh".equals(jwtTokenProvider.getType(requestRefreshToken))) {
            return ResponseEntity.badRequest().body("Invalid Refresh Token");
        }

        String jti = jwtTokenProvider.getJti(requestRefreshToken);

        // 2. DB에서 JTI로 토큰 조회
        Optional<RefreshToken> refreshTokenOpt = refreshTokenService.findByJti(jti);

        if (refreshTokenOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Refresh token not found in DB");
        }

        // 3. 토큰 만료 시간 검증 (DB에 저장된 만료 시간)
        RefreshToken refreshToken = refreshTokenService.verifyExpiration(refreshTokenOpt.get());

        // 4. 새 액세스 토큰 생성
        User user = refreshToken.getUser();
        String newAccessToken = jwtTokenProvider.createAccessToken(user.getEmail(), user.getRole());

        return ResponseEntity.ok(new TokenRefreshResponse(newAccessToken, requestRefreshToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return ResponseEntity.ok("User is not authenticated.");
        }

        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        refreshTokenService.deleteByUserId(user.getId());
        SecurityContextHolder.clearContext(); // 컨텍스트 클리어

        return ResponseEntity.ok("Successfully logged out.");
    }
}
