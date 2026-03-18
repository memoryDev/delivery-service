package com.delivery.app.domain.menu.repository;

import com.delivery.app.domain.menu.entity.Menu;

import java.util.List;

public interface MenuRepositoryCustom {

    List<Menu> findMenusByRestaurantId(Long restaurantId);
}
