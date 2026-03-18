package com.delivery.app.domain.review.repository;

import com.delivery.app.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsByOrderId(Long orderId);
    List<Review> findByRestaurantIdOrderByCreatedAtDesc(Long restaurantId);
    List<Review> findByUserIdOrderByCreatedAtDesc(Long userId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.restaurant.id = :restaurantId")
    Optional<Double> findAverageRatingByRestaurantId(@Param("restaurantId") Long restaurantId);
}
