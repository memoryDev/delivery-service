package com.delivery.app.domain.restaurant.dto.request;

import com.delivery.app.domain.restaurant.entity.RestaurantStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateRestaurantStatusRequest {

    @NotNull(message = "상태는 OPEN 또는 CLOSED만 가능합니다.")
    private RestaurantStatus status;
}
