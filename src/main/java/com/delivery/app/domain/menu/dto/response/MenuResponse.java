package com.delivery.app.domain.menu.dto.response;

import com.delivery.app.domain.menu.entity.Menu;
import com.delivery.app.domain.menu.entity.MenuStatus;
import lombok.Getter;

@Getter
public class MenuResponse {
    private Long id;
    private Long restaurantId;
    private String name;
    private String description;
    private int price;
    private MenuStatus status;

    private MenuResponse(Menu menu) {
        this.id = menu.getId();
        this.restaurantId = menu.getRestaurant().getId();
        this.name = menu.getName();
        this.description = menu.getDescription();
        this.price = menu.getPrice();
        this.status = menu.getStatus();
    }

    // Entity → DTO 변환
    public static MenuResponse from(Menu menu) {
        return new MenuResponse(menu);
    }
}
