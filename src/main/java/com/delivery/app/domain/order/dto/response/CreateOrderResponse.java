package com.delivery.app.domain.order.dto.response;

import com.delivery.app.domain.order.entity.OrderStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateOrderResponse {
    private Long orderId;
    int totalPrice;
    OrderStatus status;
}
