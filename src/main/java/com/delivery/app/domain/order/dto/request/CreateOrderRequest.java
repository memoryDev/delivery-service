package com.delivery.app.domain.order.dto.request;

import com.delivery.app.domain.order.entity.OrderItem;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreateOrderRequest {

    @NotNull(message = "배달 주소는 필수입니다.")
    private String deliveryAddress;

    String restaurantRequest;
    String RiderRequest;

    @NotEmpty(message = "주문 메뉴는 최소 1개 이상이어야 합니다.")
    @Valid
    List<OrderMenuRequest> orderItems = new ArrayList<>();

}
