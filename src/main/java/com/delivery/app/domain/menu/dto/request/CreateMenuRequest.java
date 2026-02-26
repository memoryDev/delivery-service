package com.delivery.app.domain.menu.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreateMenuRequest {

    @NotBlank(message = "메뉴 이름을 입력해 주세요.")
    private String name;

    private String description;

    @NotNull(message = "음식 값을 입력해 주세요.")
    @Min(value = 0, message = "음식 값은 0원 이상이어야 합니다.")
    private Integer price;
}
