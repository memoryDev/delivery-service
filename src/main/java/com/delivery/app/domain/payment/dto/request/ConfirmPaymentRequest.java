package com.delivery.app.domain.payment.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

@Getter
public class ConfirmPaymentRequest {

    @NotBlank(message = "결제 키는 필수 입니다.")
    private String paymentKey;

    // 요청받을땐 orderId, 자바 변수명은 orderNumber로 사용
    @JsonProperty("orderId")
    @NotBlank(message = "주문 번호는 필수입니다.")
    private String orderNumber;

    @NotNull(message = "결제 금액은 필수입니다.")
    @Positive(message = "결제 금액은 0보다 커야 합니다.")
    private Integer amount;
}
