package com.delivery.app.domain.order.service;

import com.delivery.app.domain.menu.entity.Menu;
import com.delivery.app.domain.menu.repository.MenuRepository;
import com.delivery.app.domain.order.dto.request.CreateOrderRequest;
import com.delivery.app.domain.order.dto.request.OrderMenuRequest;
import com.delivery.app.domain.order.dto.response.CreateOrderResponse;
import com.delivery.app.domain.order.entity.Order;
import com.delivery.app.domain.order.entity.OrderItem;
import com.delivery.app.domain.order.repository.OrderRepository;
import com.delivery.app.domain.restaurant.entity.Restaurant;
import com.delivery.app.domain.restaurant.entity.RestaurantStatus;
import com.delivery.app.domain.restaurant.repository.RestaurantRepository;
import com.delivery.app.domain.user.entity.User;
import com.delivery.app.domain.user.repository.UserRepository;
import com.delivery.app.global.exception.DeliveryException;
import com.delivery.app.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public CreateOrderResponse createOrder(Long userId, Long restaurantId, CreateOrderRequest request) {

        // 1. 유저 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DeliveryException(ErrorCode.USER_NOT_FOUND));

        // 2. 음식점 조회
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new DeliveryException(ErrorCode.RESTAURANT_NOT_FOUND));

        // 3. 식당 OPEN 상태인지 체크
        if (restaurant.getStatus() != RestaurantStatus.OPEN) {
            throw new DeliveryException(ErrorCode.RESTAURANT_NOT_OPEN);
        }

        // 4. 전달받은 음식 검증(해당 음식점의 메뉴가 맞는지)
        int totalMenuPrice = 0;
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderMenuRequest menuRequest : request.getOrderItems()) {
            Menu menu = menuRepository.findById(menuRequest.getMenuId())
                    .orElseThrow(() -> new DeliveryException(ErrorCode.MENU_NOT_FOUND));

            // 해당 메뉴가 고객이 요청한 식당의 메뉴가 맞는지 검증
            if (!menu.getRestaurant().getId().equals(restaurant.getId())) {
                throw new DeliveryException(ErrorCode.RESTAURANT_NOT_FOUND);
            }

            OrderItem orderItem = OrderItem.builder()
                    .menu(menu)
                    .quantity(menuRequest.getQuantity())
                    .price(menu.getPrice())
                    .build();

            orderItems.add(orderItem);
            totalMenuPrice += (menu.getPrice() * menuRequest.getQuantity());
        }

        // 5. 최소 주문 금액 검증
        if (totalMenuPrice < restaurant.getMinOrderPrice()) {
            throw new DeliveryException(ErrorCode.MIN_ORDER_PRICE_NOT_MET);
        }

        // 6. 최종 결제 금액 계산(음식 + 배달비)
        int totalPrice = totalMenuPrice + restaurant.getDeliveryFee();

        // 7. 주문 생성
        Order order = Order.create(user, restaurant, request.getDeliveryAddress(), totalPrice);

        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }

        // DB 저장
        orderRepository.save(order);

        return CreateOrderResponse.builder()
                .orderId(order.getId())
                .totalPrice(totalPrice)
                .build();
    }
}
