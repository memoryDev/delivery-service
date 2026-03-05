package com.delivery.app.domain.restaurant.service;

import com.delivery.app.domain.restaurant.dto.request.CreateRestaurantRequest;
import com.delivery.app.domain.restaurant.dto.request.UpdateRestaurantRequest;
import com.delivery.app.domain.restaurant.dto.request.UpdateRestaurantStatusRequest;
import com.delivery.app.domain.restaurant.dto.response.RestaurantResponse;
import com.delivery.app.domain.restaurant.entity.Restaurant;
import com.delivery.app.domain.restaurant.entity.RestaurantStatus;
import com.delivery.app.domain.restaurant.repository.RestaurantRepository;
import com.delivery.app.domain.user.entity.User;
import com.delivery.app.domain.user.repository.UserRepository;
import com.delivery.app.global.exception.DeliveryException;
import com.delivery.app.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    // 음식점 등록
    @Transactional
    public RestaurantResponse create(Long ownerId, CreateRestaurantRequest request) {

        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new DeliveryException(ErrorCode.USER_NOT_FOUND));

        Restaurant restaurant = Restaurant.create(
                owner,
                request.getName(),
                request.getAddress(),
                request.getPhone(),
                request.getCategory(),
                request.getMinOrderPrice(),
                request.getDeliveryFee()
        );

        restaurantRepository.save(restaurant);

        return RestaurantResponse.from(restaurant);
    }

    // 음식점 목록 조회
    public List<RestaurantResponse> getList(String category) {
        List<Restaurant> restaurants;

        if (category != null) {
            // 카테고리 필터링
            restaurants = restaurantRepository.findByCategoryAndStatus(category, RestaurantStatus.OPEN);
        } else {
            restaurants = restaurantRepository.findByStatus(RestaurantStatus.OPEN);
        }

        return restaurants.stream()
                .map(RestaurantResponse::from)
                .collect(Collectors.toList());
    }

    // 음식점 상세 조회
    public RestaurantResponse getOne(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new DeliveryException(ErrorCode.RESTAURANT_NOT_FOUND));

        return RestaurantResponse.from(restaurant);
    }

    // 음식점 수정
    @Transactional
    public RestaurantResponse update(Long ownerId, Long restaurantId, UpdateRestaurantRequest request) {

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new DeliveryException(ErrorCode.RESTAURANT_NOT_FOUND));

        // 본인 음식점인지 확인
        if (!restaurant.getOwner().getId().equals(ownerId)) {
            throw new DeliveryException(ErrorCode.RESTAURANT_NOT_OWNER);
        }

        restaurant.updateRestaurant(
                request.getName(),
                request.getAddress(),
                request.getPhone(),
                request.getCategory(),
                request.getMinOrderPrice(),
                request.getDeliveryFee()
        );

        return RestaurantResponse.from(restaurant);
    }

    // 음식점 삭제
    @Transactional
    public void delete(Long ownerId, Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new DeliveryException(ErrorCode.RESTAURANT_NOT_FOUND));

        // 본인 음식점인지 확인
        if (!restaurant.getOwner().getId().equals(ownerId)) {
            throw new DeliveryException(ErrorCode.RESTAURANT_NOT_OWNER);
        }

        // 음식점 삭제
        restaurant.delete();
    }

    // 영업 상태 변경
    @Transactional
    public RestaurantResponse updateStatus(Long ownerId, Long restaurantId, UpdateRestaurantStatusRequest request) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new DeliveryException(ErrorCode.RESTAURANT_NOT_FOUND));

        // 본인 음식점인지 확인
        if (!restaurant.getOwner().getId().equals(ownerId)) {
            throw new DeliveryException(ErrorCode.RESTAURANT_NOT_OWNER);
        }

        restaurant.updateStatus(request.getStatus());

        return RestaurantResponse.from(restaurant);

    }
}
