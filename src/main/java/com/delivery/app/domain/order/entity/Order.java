package com.delivery.app.domain.order.entity;

import com.delivery.app.domain.restaurant.entity.Restaurant;
import com.delivery.app.domain.rider.entity.Rider;
import com.delivery.app.domain.user.entity.User;
import com.delivery.app.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @Column(unique = true, nullable = false)
    private String orderNumber; // 결제 주문 번호

    // 주문한 고객
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 주문한 음식점
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    // 배달 담당 라이더(배정 전 null)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rider_id")
    private Rider rider;

    // 주문 항목 목록
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Column(nullable = false)
    private String deliveryAddress;

    // 총 결제 금액
    @Column(nullable = false)
    private int totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    private LocalDateTime deliveredAt;

    // 가게 요청사항
    @Column(length = 100)
    private String restaurantRequest;

    // 배달 요청사항
    @Column(length = 100)
    private String riderRequest;

    @Builder
    private Order(User user, Restaurant restaurant, String deliveryAddress, int totalPrice) {
        this.user = user;
        this.restaurant = restaurant;
        this.deliveryAddress = deliveryAddress;
        this.totalPrice = totalPrice;
        this.status = OrderStatus.PENDING;
    }

    @PrePersist
    public void generateOrderNumber() {
        if (this.orderNumber == null) {
            // 1. 오늘 날짜 조회
            String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

            // 2. 랜덤한 영문+숫자 8자리 설정
            String randomStr = UUID.randomUUID().toString().substring(0, 8).toUpperCase();

            // 3. 오늘날짜 + 랜덤문자열 조합(예:ORD-20260313-1A2B3C4D)
            this.orderNumber = "ORD-" + dateStr + "-" + randomStr;
        }
    }

    // 객체 생성시 사용
    public static Order create(User user, Restaurant restaurant, String deliveryAddress, int totalPrice) {
        return Order.builder()
                .user(user)
                .restaurant(restaurant)
                .deliveryAddress(deliveryAddress)
                .totalPrice(totalPrice)
                .build();
    }

    // 주문 상태 변경
    public void updateStatus(OrderStatus status) {
        this.status = status;
        // 배달 완료 시 완료 시간 기록
        if (status == OrderStatus.DELIVERED) {
            this.deliveredAt = LocalDateTime.now();
        }
    }

    // 라이더 배정
    public void assignRider(Rider rider) {
        this.rider = rider;
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.addOrder(this);
    }

}
