package com.delivery.app.domain.payment.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ConfirmPaymentResponse {
    private Long paymentId;
}
