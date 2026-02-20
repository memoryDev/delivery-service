package com.delivery.app.domain.restaurant.dto.response;

import com.delivery.app.domain.restaurant.entity.Restaurant;
import com.delivery.app.domain.restaurant.entity.RestaurantStatus;
import lombok.Getter;

@Getter
public class RestaurantResponse {

    private final Long id;
    private final String ownerName;
    private final String name;
    private final String address;
    private final String phone;
    private final String category;
    private final int minOrderPrice;
    private final int deliveryFee;
    private final double rating;
    private final RestaurantStatus status;

    private RestaurantResponse(Restaurant restaurant) {
        this.id = restaurant.getId();
        this.ownerName = restaurant.getOwner().getName();
        this.name = restaurant.getName();
        this.address = restaurant.getAddress();
        this.phone = restaurant.getPhone();
        this.category = restaurant.getCategory();
        this.minOrderPrice = restaurant.getMinOrderPrice();
        this.deliveryFee = restaurant.getDeliveryFee();
        this.rating = restaurant.getRating();
        this.status = restaurant.getStatus();
    }

    // Entity → DTO 변환
    public static RestaurantResponse from(Restaurant restaurant) {
        return new RestaurantResponse(restaurant);
    }

}
