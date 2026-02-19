package com.delivery.app.domain.rider.entity;

import com.delivery.app.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "riders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Rider {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // 연결된 유저 계정
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 차량 종류 (예: 오토바이, 자전거, 도보)
    @Column(nullable = false)
    private String vehicleType;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RiderStatus status;
    
    // Firebase 푸시 알림 토큰
    private String fcmToken;

    @Builder
    private Rider(User user, String vehicleType, String fcmToken) {
        this.user = user;
        this.vehicleType = vehicleType;
        this.fcmToken = fcmToken;
        this.status = RiderStatus.AVAILABLE;
    }

    public static Rider create(User user, String vehicleType, String fcmToken) {
        return Rider.builder()
                .user(user)
                .vehicleType(vehicleType)
                .fcmToken(fcmToken)
                .build();
    }

    // 배달 상태 변경
    public void updateStatus(RiderStatus status) {
        this.status = status;
    }

    // FCM 토큰 갱신 (앱 재설치 시 토큰 변경될 수 있음)
    // 앱으로 개발할게 아니여서 슬랙API 사용예정
    public void updateFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    
}
