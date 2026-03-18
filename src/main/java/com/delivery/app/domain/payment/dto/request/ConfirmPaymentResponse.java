package com.delivery.app.domain.payment.dto.request;

import com.delivery.app.domain.payment.entity.PaymentMethod;
import com.delivery.app.domain.payment.entity.PaymentStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ConfirmPaymentResponse {
    private Long paymentId;
    private String orderNumber;
    private int amount;
    private PaymentMethod method;
    private PaymentStatus status;
    private LocalDateTime paidAt;
}
