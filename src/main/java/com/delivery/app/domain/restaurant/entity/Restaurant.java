package com.delivery.app.domain.restaurant.entity;

import com.delivery.app.domain.user.entity.User;
import com.delivery.app.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.SoftDelete;

@Getter
@Entity
@Table(name = "restaurants")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("is_deleted = false")
public class Restaurant extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 음식점 사장님
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String phone;

    // 음식 카테고리 (예: 한식, 중식, 양식)
    @Column(nullable = false)
    private String category;

    // 최소 주문 금액
    @Column(nullable = false)
    private int minOrderPrice;

    // 배달비
    @Column(nullable = false)
    private int deliveryFee;

    @Column(nullable = false)
    private double rating = 0.0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RestaurantStatus status;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    @Builder
    private Restaurant(User owner, String name, String address, String phone, String category, int minOrderPrice, int deliveryFee) {
        this.owner = owner;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.category = category;
        this.minOrderPrice = minOrderPrice;
        this.deliveryFee = deliveryFee;
        this.rating = 0.0;
        this.status = RestaurantStatus.OPEN;
        this.isDeleted = false;
    }

    // 객체 생성시 사용
    public static Restaurant create(User owner, String name, String address, String phone, String category, int minOrderPrice, int deliveryFee) {
        return Restaurant.builder()
                .owner(owner)
                .name(name)
                .address(address)
                .phone(phone)
                .category(category)
                .minOrderPrice(minOrderPrice)
                .deliveryFee(deliveryFee)
                .build();
    }

    // 음식점 정보 수정
    public void updateRestaurant(String name, String address, String phone, String category, int minOrderPrice, int deliveryFee) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.category = category;
        this.minOrderPrice = minOrderPrice;
        this.deliveryFee = deliveryFee;
    }

    // 영업 상태 변경(오픈/마감)
    public void updateStatus(RestaurantStatus status) {
        this.status = status;
    }

    // 리뷰 등록 시 평균 평점 업데이트
    public void updateRating(double rating) {
        this.rating = rating;
    }

    // 음식점 삭제
    public void delete() {
        this.isDeleted = true;
    }
}
