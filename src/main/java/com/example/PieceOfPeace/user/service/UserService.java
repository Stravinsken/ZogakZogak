package com.example.PieceOfPeace.user.service;

import com.example.PieceOfPeace.user.dto.request.FamilyCreateRequest;
import com.example.PieceOfPeace.user.dto.request.LoginRequest;
import com.example.PieceOfPeace.user.dto.request.RegisterRequest;
import com.example.PieceOfPeace.user.entity.User;
import com.example.PieceOfPeace.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final FamilyRepository familyRepository;
    private final PasswordEncoder passwordEncoder;

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

    public User login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다."));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }
        return user;
    }

    @Transactional
    public void createFamily(FamilyCreateRequest request, String guardianUserEmail) {
        User guardian = userRepository.findByEmail(guardianUserEmail)
                .orElseThrow(() -> new IllegalArgumentException("보호자 정보를 찾을 수 없습니다."));

        User senior = userRepository.findByEmail(request.seniorUserEmail())
                .orElseThrow(() -> new IllegalArgumentException("어르신 정보를 찾을 수 없습니다."));

        Family family = Family.builder()
                .senior(senior)
                .guardian(guardian)
                .relationship(request.relationship())
                .build();

        familyRepository.save(family);
    }
}
