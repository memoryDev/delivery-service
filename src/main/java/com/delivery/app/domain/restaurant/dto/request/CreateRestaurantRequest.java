package com.delivery.app.domain.restaurant.dto.request;

import com.delivery.app.domain.restaurant.entity.CategoryType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateRestaurantRequest {

    @NotBlank(message = "음식점 이름을 입력해 주세요.")
    private String name;

    @NotBlank(message = "주소를 입력해 주세요.")
    private String address;

    @NotBlank(message = "전화번호를 입력해 주세요.")
    private String phone;

    @NotNull(message = "카테고리를 선택해 주세요.")
    private CategoryType category;

    @NotNull(message = "최소 주문 금액을 입력해 주세요.")
    @Min(value = 0, message = "최소 주문 금액은 0원 이상이어야 합니다.")
    private Integer minOrderPrice;

    @NotNull(message = "배달비를 입려해 주세요.")
    @Min(value = 0, message = "배달비는 0원 이상이어야 합니다.")
    private Integer deliveryFee;
}
