package com.delivery.app.domain.review.controller;

import com.delivery.app.domain.review.dto.request.CreateReviewRequest;
import com.delivery.app.domain.review.dto.request.UpdateReviewRequest;
import com.delivery.app.domain.review.dto.response.ReviewResponse;
import com.delivery.app.domain.review.service.ReviewService;
import com.delivery.app.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // 리뷰 작성
    @PostMapping("/api/restaurants/{restaurantId}/reviews")
    public ResponseEntity<ApiResponse<ReviewResponse>> createReview(
            @PathVariable Long restaurantId,
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody CreateReviewRequest request
    ) {
        ReviewResponse response = reviewService.createReview(userId, restaurantId, request);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    // 리뷰 수정
    @PutMapping("/api/reviews/{reviewId}")
    public ResponseEntity<ApiResponse<ReviewResponse>> updateReview(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody UpdateReviewRequest request
    ) {
        ReviewResponse response = reviewService.updateReview(userId, reviewId, request);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    // 리뷰 삭제
    @DeleteMapping("/api/reviews/{reviewId}")
    public ResponseEntity<ApiResponse<Void>> deleteReview(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal Long userId
    ) {
        reviewService.deleteReview(userId, reviewId);
        return ResponseEntity.ok(ApiResponse.ok());
    }

    // 음식점별 리뷰 목록 조회 (퍼블릭)
    @GetMapping("/api/restaurants/{restaurantId}/reviews")
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getReviewsByRestaurant(
            @PathVariable Long restaurantId
    ) {
        List<ReviewResponse> response = reviewService.getReviewsByRestaurant(restaurantId);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    // 내 리뷰 목록 조회
    @GetMapping("/api/reviews/me")
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getMyReviews(
            @AuthenticationPrincipal Long userId
    ) {
        List<ReviewResponse> response = reviewService.getMyReviews(userId);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }
}
