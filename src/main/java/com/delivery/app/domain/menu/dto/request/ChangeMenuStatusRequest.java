package com.delivery.app.domain.menu.dto.request;

import com.delivery.app.domain.menu.entity.MenuStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ChangeMenuStatusRequest {

    @NotNull(message = "변경할 상태값은 필수입니다.")
    private MenuStatus status;
}
