package com.delivery.app.domain.payment.controller;

import com.delivery.app.domain.payment.dto.request.ConfirmPaymentRequest;
import com.delivery.app.domain.payment.dto.request.ConfirmPaymentResponse;
import com.delivery.app.domain.payment.service.PaymentService;
import com.delivery.app.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    // 결제 최종 승인 요청
    @PostMapping("/confirm")
    public ResponseEntity<ApiResponse<ConfirmPaymentResponse>> confirmPayment(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody ConfirmPaymentRequest request
    ) {

        // 결제 승인 서비스 호출
        ConfirmPaymentResponse response = paymentService.confirmPayment(userId, request);

        return ResponseEntity.ok(ApiResponse.ok(response));
    }
}
