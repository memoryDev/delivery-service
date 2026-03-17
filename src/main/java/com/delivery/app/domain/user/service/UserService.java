package com.delivery.app.domain.user.service;

import com.delivery.app.domain.user.dto.request.LoginRequest;
import com.delivery.app.domain.user.dto.request.SignUpRequest;
import com.delivery.app.domain.user.dto.response.TokenResponse;
import com.delivery.app.domain.user.dto.response.UserResponse;
import com.delivery.app.domain.user.entity.Role;
import com.delivery.app.domain.user.entity.User;
import com.delivery.app.domain.user.repository.UserRepository;
import com.delivery.app.global.exception.DeliveryException;
import com.delivery.app.global.exception.ErrorCode;
import com.delivery.app.global.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    // 회원가입
    @Transactional
    public UserResponse signUp(SignUpRequest request) {

        // 이메일 중복 체크
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DeliveryException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        // 비밀번호 암호화후 유저 생성
        User user = User.create(
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getName(),
                request.getPhone(),
                request.getAddress(),
                Role.USER
        );

        userRepository.save(user);

        return UserResponse.from(user);
    }

    // 로그인
    public TokenResponse login(LoginRequest request) {

        // 이메일로 사용자 조회
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new DeliveryException(ErrorCode.USER_NOT_FOUND));

        // 비밀번호 검증
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new DeliveryException(ErrorCode.INVALID_PASSWORD);
        }

        // JWT 토큰 발급
        String accessToken = jwtTokenProvider.createAccessToken(user.getId(), user.getRole().name());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getId());

        // 반환
        return TokenResponse.of(accessToken, refreshToken);
    }

    // 내 정보 조회
    public UserResponse getMe(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DeliveryException(ErrorCode.USER_NOT_FOUND));

        return UserResponse.from(user);
    }

    // 내 정보 수정
    @Transactional
    public UserResponse updateMe(Long userId, String name, String phone, String address) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DeliveryException(ErrorCode.USER_NOT_FOUND));

        user.updateProfile(name, phone, address);

        return UserResponse.from(user);
    }
}
