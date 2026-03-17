package com.delivery.app.domain.order.service;

import com.delivery.app.domain.menu.entity.Menu;
import com.delivery.app.domain.menu.repository.MenuRepository;
import com.delivery.app.domain.order.dto.request.CreateOrderRequest;
import com.delivery.app.domain.order.dto.request.OrderMenuRequest;
import com.delivery.app.domain.order.dto.response.CreateOrderResponse;
import com.delivery.app.domain.order.entity.Order;
import com.delivery.app.domain.order.entity.OrderItem;
import com.delivery.app.domain.order.entity.OrderStatus;
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
import java.util.Map;
import java.util.stream.Collectors;

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
        
        // N+1 방지를 위해 요청된 모든 메뉴 ID 추출
        List<Long> menuIds = request.getOrderItems().stream()
                .map(OrderMenuRequest::getMenuId)
                .toList();
        
        // 한 번에 메뉴 조회 후 Map으로 변환
        Map<Long, Menu> menuMap = menuRepository.findAllById(menuIds).stream()
                .collect(Collectors.toMap(Menu::getId, menu -> menu));

        for (OrderMenuRequest menuRequest : request.getOrderItems()) {
            Menu menu = menuMap.get(menuRequest.getMenuId());
            if (menu == null) {
                throw new DeliveryException(ErrorCode.MENU_NOT_FOUND);
            }

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

    @Transactional
    public void acceptOrder(Long userId, Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new DeliveryException(ErrorCode.ORDER_NOT_FOUND));

        // OWNER 권한 검증
        if (!order.getRestaurant().getOwner().getId().equals(userId)) {
            throw new DeliveryException(ErrorCode.ORDER_ACCESS_DENIED);
        }

        if (OrderStatus.PAID != order.getStatus()) {
            throw new DeliveryException(ErrorCode.INVALID_ORDER_STATUS);
        }

        order.updateStatus(OrderStatus.ACCEPTED);
    }

    @Transactional
    public void rejectOrder(Long userId, Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new DeliveryException(ErrorCode.ORDER_NOT_FOUND));

        // OWNER 권한 검증
        if (!order.getRestaurant().getOwner().getId().equals(userId)) {
            throw new DeliveryException(ErrorCode.ORDER_ACCESS_DENIED);
        }

        if (OrderStatus.PAID != order.getStatus()) {
            throw new DeliveryException(ErrorCode.INVALID_ORDER_STATUS);
        }

        order.updateStatus(OrderStatus.CANCELLED);
    }

    @Transactional
    public void startCooking(Long userId, Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new DeliveryException(ErrorCode.ORDER_NOT_FOUND));

        // OWNER 권한 검증
        if (!order.getRestaurant().getOwner().getId().equals(userId)) {
            throw new DeliveryException(ErrorCode.ORDER_ACCESS_DENIED);
        }

        if (OrderStatus.ACCEPTED != order.getStatus()) {
            throw new DeliveryException(ErrorCode.INVALID_ORDER_STATUS);
        }

        order.updateStatus(OrderStatus.COOKING);
    }

    @Transactional
    public void startDelivery(Long userId, Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new DeliveryException(ErrorCode.ORDER_NOT_FOUND));

        // OWNER 권한 검증
        if (!order.getRestaurant().getOwner().getId().equals(userId)) {
            throw new DeliveryException(ErrorCode.ORDER_ACCESS_DENIED);
        }

        if (OrderStatus.COOKING != order.getStatus()) {
            throw new DeliveryException(ErrorCode.INVALID_ORDER_STATUS);
        }

        order.updateStatus(OrderStatus.DELIVERING);
    }

    @Transactional
    public void completeDelivery(Long userId, Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new DeliveryException(ErrorCode.ORDER_NOT_FOUND));

        // OWNER 권한 검증
        if (!order.getRestaurant().getOwner().getId().equals(userId)) {
            throw new DeliveryException(ErrorCode.ORDER_ACCESS_DENIED);
        }

        if (OrderStatus.DELIVERING != order.getStatus()) {
            throw new DeliveryException(ErrorCode.INVALID_ORDER_STATUS);
        }

        order.updateStatus(OrderStatus.DELIVERED);
    }
}
