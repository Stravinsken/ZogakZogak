package com.example.PieceOfPeace.user.controller;

import com.example.PieceOfPeace.user.dto.request.LoginRequest;
import com.example.PieceOfPeace.user.dto.request.RegisterRequest;
import com.example.PieceOfPeace.user.dto.response.LoginResponse;
import com.example.PieceOfPeace.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        userService.register(request);
        return ResponseEntity.ok("회원가입이 성공적으로 완료되었습니다.");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse loginResponse = userService.login(request);
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + loginResponse.accessToken());

        // 클라이언트가 refreshToken도 받을 수 있도록 응답 본문에 담아 전달
        return ResponseEntity.ok().headers(headers).body(loginResponse);
    }

}
