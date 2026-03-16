package com.delivery.app.domain.payment.service;

import com.delivery.app.domain.order.entity.Order;
import com.delivery.app.domain.order.entity.OrderStatus;
import com.delivery.app.domain.order.repository.OrderRepository;
import com.delivery.app.domain.payment.dto.request.ConfirmPaymentRequest;
import com.delivery.app.domain.payment.dto.request.ConfirmPaymentResponse;
import com.delivery.app.domain.payment.entity.Payment;
import com.delivery.app.domain.payment.entity.PaymentMethod;
import com.delivery.app.domain.payment.repository.PaymentRepository;
import com.delivery.app.global.exception.DeliveryException;
import com.delivery.app.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${toss.payment.secret-key}")
    private String secretKey;

    @Value("${toss.payment.url}")
    private String tossUrl;

    public ConfirmPaymentResponse confirmPayment(Long userId, ConfirmPaymentRequest request) {

        // 1. 주문 조회
        Order order = orderRepository.findByOrderNumber(request.getOrderNumber()).orElseThrow(
                () -> new DeliveryException(ErrorCode.ORDER_NOT_FOUND)
        );

        // 2. 보안 검증(본인 주문, 금액 맞는지)
        if (!order.getUser().getId().equals(userId)) {
            throw new DeliveryException(ErrorCode.INVALID_INPUT_VALUE);
        }

        if (order.getTotalPrice() != request.getAmount()) {
            throw new DeliveryException(ErrorCode.INVALID_INPUT_VALUE);
        }

        HttpHeaders headers = new HttpHeaders();
        String authKey = secretKey + ":";
        String encodedAuthKey = Base64.getEncoder().encodeToString(authKey.getBytes(StandardCharsets.UTF_8));

        // 3. 토스 API 인증 헤더 생성
        headers.set("Authorization", "Basic " + encodedAuthKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Idempotency-Key", "SAAABPQbcqjEXiDL"); // 멱등키(중복 결제 제한)

        // 4. 요청 바디 생성
        Map<String, Object> body = new HashMap<>();
        body.put("paymentKey", request.getPaymentKey());
        body.put("orderId", request.getOrderNumber());
        body.put("amount", request.getAmount());

        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(body, headers);

        try {
            // 5. POST 요청
            ResponseEntity<Map> response = restTemplate.postForEntity(tossUrl, httpEntity, Map.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                // 6. 결제 성공: 결제 생성
                Payment payment = Payment.create(order, request.getPaymentKey(), request.getAmount(), PaymentMethod.CARD);

                // 결제 상태 변경
                payment.paid();

                // 주문 상태 변경
                order.updateStatus(OrderStatus.PAID);

                // DB 저장
                Payment savedPayment = paymentRepository.save(payment);

                return ConfirmPaymentResponse.builder().paymentId(savedPayment.getId()).build();
            } else {
                throw new DeliveryException(ErrorCode.INTERNAL_SERVER_ERROR);
            }

        } catch (Exception e) {
            log.error("토스페이먼츠 결제 승인 실패: {}", e.getMessage());
            throw new DeliveryException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

    }
}
