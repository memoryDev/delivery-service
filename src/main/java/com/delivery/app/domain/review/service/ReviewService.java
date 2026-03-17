package com.delivery.app.domain.review.service;

import com.delivery.app.domain.order.entity.Order;
import com.delivery.app.domain.order.entity.OrderStatus;
import com.delivery.app.domain.order.repository.OrderRepository;
import com.delivery.app.domain.restaurant.entity.Restaurant;
import com.delivery.app.domain.restaurant.repository.RestaurantRepository;
import com.delivery.app.domain.review.dto.request.CreateReviewRequest;
import com.delivery.app.domain.review.dto.request.UpdateReviewRequest;
import com.delivery.app.domain.review.dto.response.ReviewResponse;
import com.delivery.app.domain.review.entity.Review;
import com.delivery.app.domain.review.repository.ReviewRepository;
import com.delivery.app.domain.user.entity.User;
import com.delivery.app.domain.user.repository.UserRepository;
import com.delivery.app.global.exception.DeliveryException;
import com.delivery.app.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;

    // 리뷰 작성
    @Transactional
    public ReviewResponse createReview(Long userId, Long restaurantId, CreateReviewRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DeliveryException(ErrorCode.USER_NOT_FOUND));

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new DeliveryException(ErrorCode.RESTAURANT_NOT_FOUND));

        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new DeliveryException(ErrorCode.ORDER_NOT_FOUND));

        // 주문 소유자 검증
        if (!order.getUser().getId().equals(userId)) {
            throw new DeliveryException(ErrorCode.ORDER_ACCESS_DENIED);
        }

        // 음식점 일치 검증
        if (!order.getRestaurant().getId().equals(restaurantId)) {
            throw new DeliveryException(ErrorCode.ORDER_ACCESS_DENIED);
        }

        // DELIVERED 상태 검증
        if (order.getStatus() != OrderStatus.DELIVERED) {
            throw new DeliveryException(ErrorCode.REVIEW_NOT_DELIVERED);
        }

        // 중복 리뷰 검증
        if (reviewRepository.existsByOrderId(order.getId())) {
            throw new DeliveryException(ErrorCode.REVIEW_ALREADY_EXISTS);
        }

        Review review = Review.create(user, restaurant, order, request.getRating(), request.getContent());
        reviewRepository.save(review);

        recalculateRestaurantRating(restaurant);

        return ReviewResponse.from(review);
    }

    // 리뷰 수정
    @Transactional
    public ReviewResponse updateReview(Long userId, Long reviewId, UpdateReviewRequest request) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new DeliveryException(ErrorCode.REVIEW_NOT_FOUND));

        // 작성자 검증
        if (!review.getUser().getId().equals(userId)) {
            throw new DeliveryException(ErrorCode.REVIEW_ACCESS_DENIED);
        }

        review.update(request.getRating(), request.getContent());

        recalculateRestaurantRating(review.getRestaurant());

        return ReviewResponse.from(review);
    }

    // 리뷰 삭제
    @Transactional
    public void deleteReview(Long userId, Long reviewId) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new DeliveryException(ErrorCode.REVIEW_NOT_FOUND));

        // 작성자 검증
        if (!review.getUser().getId().equals(userId)) {
            throw new DeliveryException(ErrorCode.REVIEW_ACCESS_DENIED);
        }

        Restaurant restaurant = review.getRestaurant();
        reviewRepository.delete(review);

        recalculateRestaurantRating(restaurant);
    }

    // 음식점별 리뷰 목록 조회
    public List<ReviewResponse> getReviewsByRestaurant(Long restaurantId) {

        restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new DeliveryException(ErrorCode.RESTAURANT_NOT_FOUND));

        return reviewRepository.findByRestaurantIdOrderByCreatedAtDesc(restaurantId).stream()
                .map(ReviewResponse::from)
                .toList();
    }

    // 내 리뷰 목록 조회
    public List<ReviewResponse> getMyReviews(Long userId) {
        return reviewRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(ReviewResponse::from)
                .toList();
    }

    // 음식점 평균 평점 재계산
    private void recalculateRestaurantRating(Restaurant restaurant) {
        double avg = reviewRepository.findAverageRatingByRestaurantId(restaurant.getId())
                .orElse(0.0);
        restaurant.updateRating(avg);
    }
}
