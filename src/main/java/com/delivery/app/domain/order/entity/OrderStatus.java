package com.delivery.app.domain.order.entity;

import com.delivery.app.global.exception.DeliveryException;
import com.delivery.app.global.exception.ErrorCode;

import java.util.Map;
import java.util.Set;

public enum OrderStatus {

    PENDING,    // 주문 대기
    PAID,       // 결제 완료
    ACCEPTED,   // 주문 수락
    COOKING,    // 조리 중
    DELIVERING, // 배달 중
    DELIVERED,  // 배달 완료
    CANCELLED;  // 주문 취소

    // 각 상태에서 전환 가능한 다음 상태 목록
    // DELIVERED, CANCELLED 는 종료 상태이므로 전환 불가
    private static final Map<OrderStatus, Set<OrderStatus>> ALLOWED_TRANSITIONS = Map.of(
            PENDING,    Set.of(PAID, CANCELLED),
            PAID,       Set.of(ACCEPTED, CANCELLED),
            ACCEPTED,   Set.of(COOKING, CANCELLED),
            COOKING,    Set.of(DELIVERING),
            DELIVERING, Set.of(DELIVERED),
            DELIVERED,  Set.of(),
            CANCELLED,  Set.of()
    );

    // 유효하지 않은 상태 전환 시 예외 발생
    public void validateTransition(OrderStatus next) {
        if (!ALLOWED_TRANSITIONS.get(this).contains(next)) {
            throw new DeliveryException(ErrorCode.INVALID_ORDER_STATUS);
        }
    }
}
