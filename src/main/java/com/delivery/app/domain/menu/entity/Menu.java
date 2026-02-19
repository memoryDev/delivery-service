package com.delivery.app.domain.menu.entity;

import com.delivery.app.domain.restaurant.entity.Restaurant;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "menus")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 소속 음식점
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @Column(nullable = false)
    private String name;

    private String description;

    // 메뉴 가격
    @Column(nullable = false)
    private int price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MenuStatus status;

    @Builder
    private Menu(Restaurant restaurant, String name, String description, int price) {
        this.restaurant = restaurant;
        this.name = name;
        this.description = description;
        this.price = price;
        this.status = MenuStatus.AVAILABLE;
    }

    // 객체 생성시 사용
    public static Menu create(Restaurant restaurant, String name, String description, int price) {
        return Menu.builder()
                .restaurant(restaurant)
                .name(name)
                .description(description)
                .price(price)
                .build();
    }

    // 메뉴 정보 수정
    public void update(String name, String description, int price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    // 품절 처리
    public void updateStatus(MenuStatus status) {
        this.status = status;
    }

}
