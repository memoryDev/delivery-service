package com.delivery.app.domain.payment.entity;

import com.delivery.app.domain.order.entity.Order;
import com.delivery.app.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "payments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 연결된 주문
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // 토스 결제 고유 번호 (결제 검증 시 사용)
    @Column(unique = true)
    private String paymentKey;

    // 결제 금액
    @Column(nullable = false)
    private int amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod method;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    // 결제 완료 시간
    private LocalDateTime paidAt;

    @Builder
    public Payment(Order order, String paymentKey, int amount, PaymentMethod method) {
        this.order = order;
        this.paymentKey = paymentKey;
        this.amount = amount;
        this.method = method;
        this.status = PaymentStatus.READY;
    }

    // 객체 생성시 사용
    public static Payment create(Order order, String paymentKey, int amount, PaymentMethod method) {
        return Payment.builder()
                .order(order)
                .paymentKey(paymentKey)
                .amount(amount)
                .method(method)
                .build();
    }

    // 결제 완료 처리
    public void paid() {
        this.status = PaymentStatus.PAID;
        this.paidAt = LocalDateTime.now();
    }

    // 결제 취소 처리(환불)
    public void cancel() {
        this.status = PaymentStatus.CANCELLED;
    }

    // 결제 실패 처리
    public void failed() {
        this.status = PaymentStatus.FAILED;
    }


}
