package com.delivery.app.domain.restaurant.repository;

import com.delivery.app.domain.restaurant.entity.CategoryType;
import com.delivery.app.domain.restaurant.entity.Restaurant;
import com.delivery.app.domain.restaurant.entity.RestaurantStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    // 카테고리로 음식 목록 조회
    List<Restaurant> findByCategory(CategoryType category);

    // 영업 중인 음식점 목록 조회
    List<Restaurant> findByStatus(RestaurantStatus status);

    // 사장님 ID로 음식점 목록 조회
    List<Restaurant> findByOwnerId(Long ownerId);

    // 카테고리 + 영업 상태로 음식점 목록 조회
    List<Restaurant> findByCategoryAndStatus(CategoryType category, RestaurantStatus status);
}
