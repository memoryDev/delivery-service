package com.delivery.app.domain.review.entity;

import com.delivery.app.domain.order.entity.Order;
import com.delivery.app.domain.restaurant.entity.Restaurant;
import com.delivery.app.domain.user.entity.User;
import com.delivery.app.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "reviews")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 리뷰 작성한 고객
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 리뷰 대상 음식점
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    // 연결된 주문(배달 완료된 주문만 리뷰 가능)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // 별점 (1 ~ 5)
    @Column(nullable = false)
    private int rating;

    @Column(nullable = false)
    private String content;

    @Builder
    private Review(User user, Restaurant restaurant, Order order, int rating, String content) {
       this.user = user;
       this.restaurant = restaurant;
       this.order = order;
       this.rating = rating;
       this.content = content;
    }

    // 객체 생성시 사용
    public static Review create(User user, Restaurant restaurant, Order order, int rating, String content) {
        return Review.builder()
                .user(user)
                .restaurant(restaurant)
                .order(order)
                .rating(rating)
                .content(content)
                .build();
    }

    // 리뷰 수정
    public void update(int rating, String content) {
        this.rating = rating;
        this.content = content;
    }
}
