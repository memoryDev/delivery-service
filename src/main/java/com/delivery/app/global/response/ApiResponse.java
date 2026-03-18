package com.delivery.app.global.response;

import lombok.Getter;

@Getter
public class ApiResponse<T> {

    // 요청 성공 여부
    private final boolean success;

    // 응답 데이터
    private final T data;

    // 에러 메시지
    private final String message;

    private ApiResponse(boolean success, T data, String message) {
        this.success = success;
        this.data = data;
        this.message = message;
    }

    // 성공 응답(데이터 없음)
    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, data, null);
    }

    // 성공 응답(데이터 없음)
    public static <T> ApiResponse<T> ok() {
        return new ApiResponse<>(true, null, null);
    }

    // 실패 응답
    public static <T> ApiResponse<T> fail(String message) {
        return new ApiResponse<>(false, null, message);
    }
}
