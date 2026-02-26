package com.delivery.app.domain.user.controller;

import com.delivery.app.domain.user.dto.request.LoginRequest;
import com.delivery.app.domain.user.dto.request.SignUpRequest;
import com.delivery.app.domain.user.dto.request.UpdateMeRequest;
import com.delivery.app.domain.user.dto.response.TokenResponse;
import com.delivery.app.domain.user.dto.response.UserResponse;
import com.delivery.app.domain.user.service.UserService;
import com.delivery.app.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/api/auth/signup")
    public ResponseEntity<ApiResponse<UserResponse>> signUp(@Valid @RequestBody SignUpRequest request) {
        UserResponse response = userService.signUp(request);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    // 로그인
    @PostMapping("/api/auth/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(@Valid @RequestBody LoginRequest request) {
        TokenResponse login = userService.login(request);
        return ResponseEntity.ok(ApiResponse.ok(login));
    }

    // 내 정보 조회
    @GetMapping("/api/users/me")
    public ResponseEntity<ApiResponse<UserResponse>> getMe(@AuthenticationPrincipal Long userId) {
        UserResponse response = userService.getMe(userId);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    // 내 정보 수정
    @PutMapping("/api/users/me")
    public ResponseEntity<ApiResponse<UserResponse>> updateMe(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody UpdateMeRequest request) {
        UserResponse response = userService.updateMe(userId, request.getName(), request.getPhone(), request.getAddress());
        return ResponseEntity.ok(ApiResponse.ok(response));
    }
}