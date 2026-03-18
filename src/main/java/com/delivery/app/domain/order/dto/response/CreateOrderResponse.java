package com.delivery.app.domain.order.dto.response;

import com.delivery.app.domain.order.entity.OrderStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateOrderResponse {
    private Long orderId;
    private String orderNumber;
    private int totalPrice;
    private OrderStatus status;
}
