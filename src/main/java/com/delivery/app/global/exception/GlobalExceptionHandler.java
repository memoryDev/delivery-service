package com.delivery.app.global.exception;

import com.delivery.app.global.response.ApiResponse;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 커스텀 예외 처리
    @ExceptionHandler(DeliveryException.class)
    public ResponseEntity<ApiResponse<Void>> handleDeliveryException(DeliveryException e) {
        log.error("DeliveryException: {}", e.getMessage());
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ApiResponse.fail(errorCode.getMessage()));
    }

    // @Valid 유효성 검사 실패 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult()
                .getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(java.util.stream.Collectors.joining(", "));
        log.error("ValidationException: {}", message);
        return ResponseEntity
                .badRequest()
                .body(ApiResponse.fail(message));
    }

    // JSON 파싱 오류 실패 처리
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        // 기본 에러 메시지
        String errorMessage = "요청 본문의 형식이 올바르지 않습니다.";

        // 근본 원인이 Jackson의 타입 변환 실패(InvalidFormatException)인지 확인
        if (e.getCause() instanceof InvalidFormatException invalidFormatException) {
            Class<?> targetType = invalidFormatException.getTargetType();

            // 변환을 시도했던 대상 타입이 Enum인지 확인
            if (targetType != null && targetType.isEnum()) {
                // Enum에 정의된 정상적인 값들을 추출 (예: [OPEN, CLOSED])
                Object[] enumConstants = targetType.getEnumConstants();

                errorMessage = String.format(
                        "잘못된 상태값입니다. 허용되는 값: %s (입력값: '%s')",
                        Arrays.toString(enumConstants),
                        invalidFormatException.getValue()
                );
            }
        }

        // 다른 예외 처리기들과 동일하게 에러 로깅
        log.error("HttpMessageNotReadableException: {}", errorMessage);

        return ResponseEntity
                // ErrorCode.INVALID_INPUT_VALUE의 상태 코드(400) 사용
                .status(ErrorCode.INVALID_INPUT_VALUE.getStatus())
                // ApiResponse.fail()에는 메시지만 1개 전달하여 컴파일 에러 방지
                .body(ApiResponse.fail(errorMessage));
    }

    // 그외 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        log.error("Exception: {}", e.getMessage());
        return ResponseEntity
                .internalServerError()
                .body(ApiResponse.fail("서버 오류가 발생했습니다."));
    }




}
