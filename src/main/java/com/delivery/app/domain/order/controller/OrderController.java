package com.delivery.app.domain.order.controller;

import com.delivery.app.domain.order.dto.request.CreateOrderRequest;
import com.delivery.app.domain.order.dto.response.CreateOrderResponse;
import com.delivery.app.domain.order.service.OrderService;
import com.delivery.app.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping("/api/orders/{orderId}/accept")
    public ResponseEntity<ApiResponse<Void>> acceptOrder(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long orderId) {

        orderService.acceptOrder(userId, orderId);

        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @PatchMapping("/api/orders/{orderId}/reject")
    public ResponseEntity<ApiResponse<Void>> rejectOrder(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long orderId) {

        orderService.rejectOrder(userId, orderId);

        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @PatchMapping("/api/orders/{orderId}/cooking")
    public ResponseEntity<ApiResponse<Void>> startCooking(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long orderId) {

        orderService.startCooking(userId, orderId);

        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @PatchMapping("/api/orders/{orderId}/delivery")
    public ResponseEntity<ApiResponse<Void>> startDelivery(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long orderId) {

        orderService.startDelivery(userId, orderId);

        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @PatchMapping("/api/orders/{orderId}/complete")
    public ResponseEntity<ApiResponse<Void>> completeDelivery(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long orderId) {

        orderService.completeDelivery(userId, orderId);

        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}
