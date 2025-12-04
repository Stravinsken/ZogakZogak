package com.example.PieceOfPeace.jwt.service;

import com.example.PieceOfPeace.jwt.JwtTokenProvider;
import com.example.PieceOfPeace.jwt.entity.RefreshToken;
import com.example.PieceOfPeace.jwt.repository.RefreshTokenRepository;
import com.example.PieceOfPeace.user.entity.User;
import com.example.PieceOfPeace.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${jwt.refresh-expiration-ms}")
    private long refreshTokenDurationMs;

    @Transactional
    public String createRefreshToken(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));

        // 기존 리프레시 토큰이 있다면 삭제
        refreshTokenRepository.findByUserId(user.getId()).ifPresent(refreshTokenRepository::delete);

        String tokenValue = jwtTokenProvider.createRefreshToken(email);
        String jti = jwtTokenProvider.getJti(tokenValue);
        Instant expiryDate = Instant.now().plusMillis(refreshTokenDurationMs);

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(jti) // JTI 저장
                .expiryDate(expiryDate)
                .build();

        refreshTokenRepository.save(refreshToken);
        return tokenValue;
    }

    @Transactional(readOnly = true)
    public Optional<RefreshToken> findByJti(String jti) {
        return refreshTokenRepository.findByToken(jti);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token was expired. Please make a new signin request.");
        }
        return token;
    }

    @Transactional
    public void deleteByUserId(Long userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }
}
