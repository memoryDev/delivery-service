package com.delivery.app.domain.order.entity;

import com.delivery.app.domain.menu.entity.Menu;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "order_items")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 소속 주문
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // 주문한 메뉴
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    // 주문 수량
    @Column(nullable = false)
    private int quantity;

    // 주문 시점의 가격 (메뉴 가겨 변동에 대비해 별도 저장)
    @Column(nullable = false)
    private int price;

    @Builder
    public OrderItem(Order order, Menu menu, int quantity, int price) {
        this.order = order;
        this.menu = menu;
        this.quantity = quantity;
        this.price = price;
    }

    // 객체 생성시 사용
    public static OrderItem create(Order order, Menu menu, int quantity) {
        return OrderItem.builder()
                .order(order)
                .menu(menu)
                .quantity(quantity)
                .price(quantity * menu.getPrice())
                .build();
    }

    public void addOrder(Order order) {
        this.order = order;
    }


}
