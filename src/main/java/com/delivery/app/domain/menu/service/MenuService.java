package com.delivery.app.domain.menu.service;

import com.delivery.app.domain.menu.dto.request.ChangeMenuStatusRequest;
import com.delivery.app.domain.menu.dto.request.CreateMenuRequest;
import com.delivery.app.domain.menu.dto.response.MenuResponse;
import com.delivery.app.domain.menu.entity.Menu;
import com.delivery.app.domain.menu.repository.MenuRepository;
import com.delivery.app.domain.restaurant.entity.Restaurant;
import com.delivery.app.domain.restaurant.repository.RestaurantRepository;
import com.delivery.app.global.exception.DeliveryException;
import com.delivery.app.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuService {

    private final RestaurantRepository restaurantRepository;

    private final MenuRepository menuRepository;

    // 메뉴 등록
    @Transactional
    public MenuResponse create(Long restaurantId, Long ownerId, CreateMenuRequest request) {

        // 1. 음식점 조회
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new DeliveryException(ErrorCode.RESTAURANT_NOT_FOUND));

        // 2. 음식점 사장님인지 조회
        if (!restaurant.getOwner().getId().equals(ownerId)) {
            throw new DeliveryException(ErrorCode.RESTAURANT_NOT_OWNER);
        }

        // 2. 메뉴 추가
        Menu menu = Menu.create(restaurant, request.getName(), request.getDescription(), request.getPrice());
        menuRepository.save(menu);

        // 3. DTO 변환
        return MenuResponse.from(menu);
    }

    // 메뉴 목록 조회
    public List<MenuResponse> getMenu(Long restaurantId) {

        restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new DeliveryException(ErrorCode.RESTAURANT_NOT_FOUND));

        List<Menu> menus = menuRepository.findMenusByRestaurantId(restaurantId);

        return menus.stream()
                .map(MenuResponse::from)
                .toList();
    }

    // 메뉴 상태 변경
    @Transactional
    public void changeStatus(Long menuId, Long ownerId, ChangeMenuStatusRequest request) {

        // 메뉴 조회
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new DeliveryException(ErrorCode.MENU_NOT_FOUND));

        // 음식점 사장님인지 조회
        if (!menu.getRestaurant().getOwner().getId().equals(ownerId)) {
            throw new DeliveryException(ErrorCode.RESTAURANT_NOT_OWNER);
        }

        // 상태 변경
        menu.updateStatus(request.getStatus());
    }
}
