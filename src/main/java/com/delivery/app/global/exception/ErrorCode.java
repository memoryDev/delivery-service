package com.delivery.app.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(400, "잘못된 입력값입니다."),
    INVALID_TYPE_VALUE(400, "유효하지 않은 타입의 값이 입력되었습니다."),

    // User
    USER_NOT_FOUND(404, "사용자를 찾을 수 없습니다."),
    EMAIL_ALREADY_EXISTS(409, "이미 사용 중인 이메일입니다."),
    INVALID_PASSWORD(400, "비밀번호가 일치하지 않습니다."),
    UNAUTHORIZED(401, "인증이 필요합니다."),
    FORBIDDEN(403, "접근 권한이 없습니다."),

    // Restaurant
    RESTAURANT_NOT_FOUND(404, "음식점을 찾을 수 없습니다."),
    RESTAURANT_NOT_OWNER(403, "음식점 사장님만 접근 가능합니다."),
    RESTAURANT_NOT_OPEN(400, "현재 영업 중인 음식점이 아닙니다."),

    // Menu
    MENU_NOT_FOUND(404, "메뉴를 찾을 수 없습니다."),
    MENU_SOLD_OUT(400, "품절된 메뉴입니다."),

    // Order
    ORDER_NOT_FOUND(404, "주문을 찾을 수 없습니다."),
    INVALID_ORDER_STATUS(400, "유효하지 않은 주문 상태입니다."),
    MIN_ORDER_PRICE_NOT_MET(400, "최소 주문 금액을 충족하지 않습니다."),

    // Payment
    PAYMENT_NOT_FOUND(404, "결제 정보를 찾을 수 없습니다."),
    PAYMENT_FAILED(500, "결제에 실패했습니다."),
    PAYMENT_AMOUNT_MISMATCH(400, "결제 금액이 일치하지 않습니다."),

    // Review
    REVIEW_NOT_FOUND(404, "리뷰를 찾을 수 없습니다."),
    REVIEW_ALREADY_EXISTS(409, "이미 리뷰를 작성했습니다."),
    REVIEW_NOT_DELIVERED(400, "배달 완료된 주문만 리뷰 작성이 가능합니다."),

    // Rider
    RIDER_NOT_FOUND(404, "라이더를 찾을 수 없습니다."),
    RIDER_NOT_AVAILABLE(400, "배달 가능한 라이더가 없습니다.");

    private final int status;
    private final String message;
}
