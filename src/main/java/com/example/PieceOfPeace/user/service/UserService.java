package com.example.PieceOfPeace.user.service;

import com.example.PieceOfPeace.jwt.JwtTokenProvider;
import com.example.PieceOfPeace.jwt.service.RefreshTokenService;
import com.example.PieceOfPeace.user.dto.request.LoginRequest;
import com.example.PieceOfPeace.user.dto.request.RegisterRequest;
import com.example.PieceOfPeace.user.dto.response.LoginResponse;
import com.example.PieceOfPeace.user.entity.User;
import com.example.PieceOfPeace.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public void register(RegisterRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        User user = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .name(request.name())
                .number(request.number())
                .role(request.role())
                .build();

        userRepository.save(user);
    }

    @Transactional
    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다."));


        String accessToken = jwtTokenProvider.createAccessToken(user.getEmail(), user.getRole());
        String refreshToken = refreshTokenService.createRefreshToken(user.getEmail());

        return new LoginResponse(accessToken, refreshToken);
    }
}
