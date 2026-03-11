package com.delivery.app.domain.order.controller;

import com.delivery.app.domain.order.dto.request.CreateOrderRequest;
import com.delivery.app.domain.order.dto.response.CreateOrderResponse;
import com.delivery.app.domain.order.service.OrderService;
import com.delivery.app.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/api/restaurants/{restaurantId}/orders")
    public ResponseEntity<ApiResponse<CreateOrderResponse>> createOrder(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long restaurantId,
            @Valid @RequestBody CreateOrderRequest request
    ) {

        CreateOrderResponse response = orderService.createOrder(userId, restaurantId, request);

        return ResponseEntity.ok(ApiResponse.ok(response));
    }
}
