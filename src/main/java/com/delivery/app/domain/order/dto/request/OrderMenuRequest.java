package com.delivery.app.domain.order.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
public class OrderMenuRequest {
    @NotNull(message = "메뉴 ID는 필수입니다.")
    private Long menuId;

    @NotNull(message = "수량을 입력해주세요.")
    @Positive(message = "수량을 1이상 입력해 주세요.")
    private Integer quantity;
}
