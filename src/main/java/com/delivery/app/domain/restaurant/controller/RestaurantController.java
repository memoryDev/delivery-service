package com.delivery.app.domain.restaurant.controller;

import com.delivery.app.domain.restaurant.dto.request.CreateRestaurantRequest;
import com.delivery.app.domain.restaurant.dto.request.UpdateRestaurantRequest;
import com.delivery.app.domain.restaurant.dto.response.RestaurantResponse;
import com.delivery.app.domain.restaurant.entity.RestaurantStatus;
import com.delivery.app.domain.restaurant.service.RestaurantService;
import com.delivery.app.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    // 음식점 등록 (OWNER만 가능)
    @PostMapping("/api/owner/restaurants")
    public ResponseEntity<ApiResponse<RestaurantResponse>> create(
            @AuthenticationPrincipal Long ownerId,
            @Valid @RequestBody CreateRestaurantRequest request) {
        RestaurantResponse response = restaurantService.create(ownerId, request);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    // 음식점 목록 조회 (누구나 가능)
    @GetMapping("/api/restaurants")
    public ResponseEntity<ApiResponse<List<RestaurantResponse>>> getList(
            @RequestParam(required = false) String category) {
        List<RestaurantResponse> response = restaurantService.getList(category);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    // 음식점 상세 조회 (누구나 가능)
    @GetMapping("/api/restaurants/{restaurantId}")
    public ResponseEntity<ApiResponse<RestaurantResponse>> getOne(
            @PathVariable Long restaurantId) {
        RestaurantResponse response = restaurantService.getOne(restaurantId);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    // 음식점 수정 (OWNER만 가능)
    @PutMapping("/api/owner/restaurants/{restaurantId}")
    public ResponseEntity<ApiResponse<RestaurantResponse>> update(
            @AuthenticationPrincipal Long ownerId,
            @PathVariable Long restaurantId,
            @Valid @RequestBody UpdateRestaurantRequest request) {
        RestaurantResponse response = restaurantService.update(ownerId, restaurantId, request);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    // 음식점 삭제 (OWNER만 가능)
    @DeleteMapping("/api/owner/restaurants/{restaurantId}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @AuthenticationPrincipal Long ownerId,
            @PathVariable Long restaurantId) {
        restaurantService.delete(ownerId, restaurantId);
        return ResponseEntity.ok(ApiResponse.ok());
    }

    // 영업 상태 변경 (OWNER만 가능)
    @PatchMapping("/api/owner/restaurants/{restaurantId}/status")
    public ResponseEntity<ApiResponse<RestaurantResponse>> updateStatus(
            @AuthenticationPrincipal Long ownerId,
            @PathVariable Long restaurantId,
            @RequestParam RestaurantStatus status) {
        RestaurantResponse response = restaurantService.updateStatus(ownerId, restaurantId, status);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }
}
