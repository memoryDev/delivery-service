# 코드 컨벤션

## 패키지 구조

도메인형 패키지 구조를 사용합니다.

```
src/main/java/com/delivery/
│
├── domain/
│   ├── user/
│   │   ├── controller/
│   │   ├── service/
│   │   ├── repository/
│   │   ├── entity/
│   │   └── dto/
│   │       ├── request/
│   │       └── response/
│   ├── restaurant/
│   ├── menu/
│   ├── order/
│   ├── payment/
│   ├── review/
│   └── rider/
│
├── global/
│   ├── config/          # Spring 설정 클래스
│   ├── exception/       # 공통 예외 처리
│   ├── response/        # 공통 응답 형식
│   ├── security/        # Spring Security, JWT
│   └── util/            # 유틸리티 클래스
│
└── DeliveryApplication.java
```

---

## 네이밍 규칙

### 클래스

| 종류 | 규칙 | 예시 |
|---|---|---|
| Controller | `{도메인}Controller` | `UserController` |
| Service | `{도메인}Service` | `OrderService` |
| Repository | `{도메인}Repository` | `RestaurantRepository` |
| Entity | `{도메인}` | `User`, `Order` |
| Request DTO | `{동사}{도메인}Request` | `CreateOrderRequest` |
| Response DTO | `{도메인}Response` | `OrderResponse` |
| Exception | `{도메인}{이유}Exception` | `OrderNotFoundException` |

### 메서드

```java
// 조회
getUserById()
findOrdersByUserId()

// 생성
createOrder()
registerRestaurant()

// 수정
updateMenu()
changeOrderStatus()

// 삭제
deleteReview()
cancelOrder()
```

---

## API 응답 형식

모든 API는 공통 응답 형식을 사용합니다.

```json
// 성공
{
  "success": true,
  "data": { ... },
  "message": null
}

// 실패
{
  "success": false,
  "data": null,
  "message": "주문을 찾을 수 없습니다."
}
```

```java
// ApiResponse.java
@Getter
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private T data;
    private String message;

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, data, null);
    }

    public static <T> ApiResponse<T> fail(String message) {
        return new ApiResponse<>(false, null, message);
    }
}
```

---

## 예외 처리

### 커스텀 예외

```java
// 기본 예외 클래스
public class DeliveryException extends RuntimeException {
    private final ErrorCode errorCode;

    public DeliveryException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}

// 도메인별 예외
public class OrderNotFoundException extends DeliveryException {
    public OrderNotFoundException() {
        super(ErrorCode.ORDER_NOT_FOUND);
    }
}
```

### 에러 코드

```java
@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // User
    USER_NOT_FOUND(404, "사용자를 찾을 수 없습니다."),
    EMAIL_ALREADY_EXISTS(409, "이미 사용 중인 이메일입니다."),

    // Order
    ORDER_NOT_FOUND(404, "주문을 찾을 수 없습니다."),
    INVALID_ORDER_STATUS(400, "유효하지 않은 주문 상태입니다."),

    // Restaurant
    RESTAURANT_NOT_FOUND(404, "음식점을 찾을 수 없습니다."),

    // Payment
    PAYMENT_FAILED(500, "결제에 실패했습니다."),
    PAYMENT_AMOUNT_MISMATCH(400, "결제 금액이 일치하지 않습니다.");

    private final int status;
    private final String message;
}
```

---

## Entity 규칙

```java
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 연관관계는 지연로딩 기본
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // Enum은 STRING으로 저장
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    // 생성자 대신 정적 팩토리 메서드 사용
    public static Order create(User user, Restaurant restaurant, String address) {
        Order order = new Order();
        order.user = user;
        order.restaurant = restaurant;
        order.deliveryAddress = address;
        order.status = OrderStatus.PENDING;
        return order;
    }
}
```

### BaseEntity (공통 시간 컬럼)

```java
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
```

---

## 기타 규칙

- **Lombok** 적극 활용 (`@Getter`, `@Builder`, `@RequiredArgsConstructor`)
- **setter 사용 금지** — 비즈니스 메서드로 상태 변경
- **양방향 연관관계 지양** — 필요한 경우에만 사용
- **트랜잭션** — Service 메서드에 `@Transactional` 명시
- **테스트** — 주요 비즈니스 로직은 단위 테스트 작성
