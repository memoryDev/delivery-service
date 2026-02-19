package com.delivery.app.domain.order.entity;

public enum OrderStatus {
    PENDING,    // 주문 대기
    ACCEPTED,   // 주문 수락
    COOKING,    // 조리 중
    DELIVERING, // 배달 중
    DELIVERED,  // 배달 완료
    CANCELLED   // 주문 취소
}
