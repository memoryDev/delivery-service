# API 명세서

> 모든 응답은 `ApiResponse<T>` 로 래핑됩니다.
> 인증이 필요한 API는 `Authorization: Bearer {accessToken}` 헤더가 필요합니다.

---

## 목차

- [AUTH](#auth)
- [USER](#user)
- [RESTAURANT](#restaurant)
- [MENU](#menu)
- [ORDER](#order)
- [PAYMENT](#payment)
- [REVIEW](#review)

---

## AUTH

### POST `/api/auth/signup` — 회원가입

**인증:** 불필요

**Request Body**

| 설명 | 필드 | 타입 | 필수 | 제약 |
|------|------|------|:----:|------|
| 이메일 | email | String | ✅ | 이메일 형식 |
| 비밀번호 | password | String | ✅ | |
| 이름 | name | String | ✅ | |
| 전화번호 | phone | String | ✅ | |
| 주소 | address | String | ❌ | |
| 역할 | role | Role | ✅ | `USER` \| `OWNER` \| `RIDER` |

**Response** `UserResponse`

| 설명 | 필드 | 타입 |
|------|------|------|
| 사용자 ID | id | Long |
| 이메일 | email | String |
| 이름 | name | String |
| 전화번호 | phone | String |
| 주소 | address | String |
| 역할 | role | Role |

---

### POST `/api/auth/login` — 로그인

**인증:** 불필요

**Request Body**

| 설명 | 필드 | 타입 | 필수 | 제약 |
|------|------|------|:----:|------|
| 이메일 | email | String | ✅ | 이메일 형식 |
| 비밀번호 | password | String | ✅ | |

**Response** `TokenResponse`

| 설명 | 필드 | 타입 |
|------|------|------|
| 액세스 토큰 | accessToken | String |
| 리프레시 토큰 | refreshToken | String |

---

## USER

### GET `/api/users/me` — 내 정보 조회

**인증:** 필요

Request 파라미터 없음

**Response** `UserResponse`

| 설명 | 필드 | 타입 |
|------|------|------|
| 사용자 ID | id | Long |
| 이메일 | email | String |
| 이름 | name | String |
| 전화번호 | phone | String |
| 주소 | address | String |
| 역할 | role | Role |

---

### PUT `/api/users/me` — 내 정보 수정

**인증:** 필요

**Request Body**

| 설명 | 필드 | 타입 | 필수 |
|------|------|------|:----:|
| 이름 | name | String | ✅ |
| 전화번호 | phone | String | ✅ |
| 주소 | address | String | ❌ |

**Response** `UserResponse` (위와 동일)

---

## RESTAURANT

### POST `/api/owner/restaurants` — 식당 등록

**인증:** 필요 (OWNER)

**Request Body**

| 설명 | 필드 | 타입 | 필수 | 제약 |
|------|------|------|:----:|------|
| 식당 이름 | name | String | ✅ | |
| 식당 주소 | address | String | ✅ | |
| 식당 전화번호 | phone | String | ✅ | |
| 카테고리 | category | String | ✅ | |
| 최소 주문 금액 | minOrderPrice | Integer | ✅ | ≥ 0 |
| 배달비 | deliveryFee | Integer | ✅ | ≥ 0 |

**Response** `RestaurantResponse`

| 설명 | 필드 | 타입 |
|------|------|------|
| 식당 ID | id | Long |
| 사장님 이름 | ownerName | String |
| 식당 이름 | name | String |
| 식당 주소 | address | String |
| 식당 전화번호 | phone | String |
| 카테고리 | category | String |
| 최소 주문 금액 | minOrderPrice | int |
| 배달비 | deliveryFee | int |
| 평점 | rating | double |
| 영업 상태 | status | RestaurantStatus |

---

### GET `/api/restaurants` — 식당 목록 조회

**인증:** 불필요

**Query Parameter**

| 설명 | 파라미터 | 타입 | 필수 |
|------|----------|------|:----:|
| 카테고리 필터 | category | String | ❌ |

**Response** `List<RestaurantResponse>` (위와 동일)

---

### GET `/api/restaurants/{restaurantId}` — 식당 상세 조회

**인증:** 불필요

**Path Variable**

| 설명 | 파라미터 | 타입 |
|------|----------|------|
| 식당 ID | restaurantId | Long |

**Response** `RestaurantResponse` (위와 동일)

---

### PUT `/api/owner/restaurants/{restaurantId}` — 식당 정보 수정

**인증:** 필요 (OWNER)

**Path Variable**

| 설명 | 파라미터 | 타입 |
|------|----------|------|
| 식당 ID | restaurantId | Long |

**Request Body**

| 설명 | 필드 | 타입 | 필수 | 제약 |
|------|------|------|:----:|------|
| 식당 이름 | name | String | ✅ | |
| 식당 주소 | address | String | ✅ | |
| 식당 전화번호 | phone | String | ✅ | |
| 카테고리 | category | String | ✅ | |
| 최소 주문 금액 | minOrderPrice | Integer | ✅ | ≥ 0 |
| 배달비 | deliveryFee | Integer | ✅ | ≥ 0 |

**Response** `RestaurantResponse` (위와 동일)

---

### DELETE `/api/owner/restaurants/{restaurantId}` — 식당 삭제

**인증:** 필요 (OWNER)

**Path Variable**

| 설명 | 파라미터 | 타입 |
|------|----------|------|
| 식당 ID | restaurantId | Long |

**Response:** 없음

---

### PATCH `/api/owner/restaurants/{restaurantId}/status` — 영업 상태 변경

**인증:** 필요 (OWNER)

**Path Variable**

| 설명 | 파라미터 | 타입 |
|------|----------|------|
| 식당 ID | restaurantId | Long |

**Request Body**

| 설명 | 필드 | 타입 | 필수 |
|------|------|------|:----:|
| 영업 상태 | status | RestaurantStatus | ✅ |

**Response** `RestaurantResponse` (위와 동일)

---

## MENU

### POST `/api/owner/restaurants/{restaurantId}/menus` — 메뉴 등록

**인증:** 필요 (OWNER)

**Path Variable**

| 설명 | 파라미터 | 타입 |
|------|----------|------|
| 식당 ID | restaurantId | Long |

**Request Body**

| 설명 | 필드 | 타입 | 필수 | 제약 |
|------|------|------|:----:|------|
| 메뉴 이름 | name | String | ✅ | |
| 메뉴 설명 | description | String | ❌ | |
| 가격 | price | Integer | ✅ | ≥ 0 |

**Response** `MenuResponse`

| 설명 | 필드 | 타입 |
|------|------|------|
| 메뉴 ID | id | Long |
| 식당 ID | restaurantId | Long |
| 메뉴 이름 | name | String |
| 메뉴 설명 | description | String |
| 가격 | price | int |
| 메뉴 상태 | status | MenuStatus |

---

### GET `/api/restaurants/{restaurantId}/menus` — 메뉴 목록 조회

**인증:** 불필요

**Path Variable**

| 설명 | 파라미터 | 타입 |
|------|----------|------|
| 식당 ID | restaurantId | Long |

**Response** `List<MenuResponse>` (위와 동일)

---

### PATCH `/api/owner/menus/{menuId}/status` — 메뉴 상태 변경

**인증:** 필요 (OWNER)

**Path Variable**

| 설명 | 파라미터 | 타입 |
|------|----------|------|
| 메뉴 ID | menuId | Long |

**Request Body**

| 설명 | 필드 | 타입 | 필수 |
|------|------|------|:----:|
| 메뉴 상태 | status | MenuStatus | ✅ |

**Response:** 없음

---

## ORDER

> **주문 상태 흐름**
> ```
> PENDING → PAID → ACCEPTED → COOKING → DELIVERING → DELIVERED
>                ↘ REJECTED
> PENDING → CANCELLED
> ```

### POST `/api/restaurants/{restaurantId}/orders` — 주문 생성

**인증:** 필요

**Path Variable**

| 설명 | 파라미터 | 타입 |
|------|----------|------|
| 식당 ID | restaurantId | Long |

**Request Body**

| 설명 | 필드 | 타입 | 필수 | 제약 |
|------|------|------|:----:|------|
| 배달 주소 | deliveryAddress | String | ✅ | |
| 식당 요청사항 | restaurantRequest | String | ❌ | |
| 라이더 요청사항 | riderRequest | String | ❌ | |
| 주문 항목 목록 | orderItems | List\<OrderMenuRequest\> | ✅ | 최소 1개 |

**OrderMenuRequest**

| 설명 | 필드 | 타입 | 필수 | 제약 |
|------|------|------|:----:|------|
| 메뉴 ID | menuId | Long | ✅ | |
| 주문 수량 | quantity | Integer | ✅ | 양수 |

**Response** `CreateOrderResponse`

| 설명 | 필드 | 타입 |
|------|------|------|
| 주문 ID | orderId | Long |
| 총 결제 금액 | totalPrice | int |
| 주문 상태 | status | OrderStatus |

---

### PATCH `/api/orders/{orderId}/accept` — 주문 수락

**인증:** 필요

**Path Variable**

| 설명 | 파라미터 | 타입 |
|------|----------|------|
| 주문 ID | orderId | Long |

**Response:** 없음

---

### PATCH `/api/orders/{orderId}/reject` — 주문 거절

**인증:** 필요

**Path Variable**

| 설명 | 파라미터 | 타입 |
|------|----------|------|
| 주문 ID | orderId | Long |

**Response:** 없음

---

### PATCH `/api/orders/{orderId}/cooking` — 조리 시작

**인증:** 필요

**Path Variable**

| 설명 | 파라미터 | 타입 |
|------|----------|------|
| 주문 ID | orderId | Long |

**Response:** 없음

---

### PATCH `/api/orders/{orderId}/delivery` — 배달 시작

**인증:** 필요

**Path Variable**

| 설명 | 파라미터 | 타입 |
|------|----------|------|
| 주문 ID | orderId | Long |

**Response:** 없음

---

### PATCH `/api/orders/{orderId}/complete` — 배달 완료

**인증:** 필요

**Path Variable**

| 설명 | 파라미터 | 타입 |
|------|----------|------|
| 주문 ID | orderId | Long |

**Response:** 없음

---

## PAYMENT

### POST `/api/payments/confirm` — 결제 승인

**인증:** 필요

**Request Body**

| 설명 | 필드 | 타입 | 필수 | 제약 |
|------|------|------|:----:|------|
| 토스 페이먼츠 결제 키 | paymentKey | String | ✅ | |
| 주문 번호 | orderId | String | ✅ | JSON key: `orderId` |
| 결제 금액 | amount | Integer | ✅ | 양수 |

**Response** `ConfirmPaymentResponse`

| 설명 | 필드 | 타입 |
|------|------|------|
| 결제 ID | paymentId | Long |
| 주문 번호 | orderNumber | String |
| 결제 금액 | amount | int |
| 결제 수단 | method | PaymentMethod |
| 결제 상태 | status | PaymentStatus |
| 결제 완료 일시 | paidAt | LocalDateTime |

---

## CATEGORY

### GET `/api/categories` — 카테고리 목록 조회

**인증:** 불필요

Request 파라미터 없음

**Response** `List<String>`

음식점 카테고리 전체 목록을 반환합니다.

가능한 값: `KOREAN`, `CHINESE`, `WESTERN`, `JAPANESE`, `CHICKEN`, `PIZZA`, `BUNSIK`, `CAFE`, `ETC`

---

## REVIEW

### POST `/api/restaurants/{restaurantId}/reviews` — 리뷰 작성

**인증:** 필요

**Path Variable**

| 설명 | 파라미터 | 타입 |
|------|----------|------|
| 식당 ID | restaurantId | Long |

**Request Body**

| 설명 | 필드 | 타입 | 필수 | 제약 |
|------|------|------|:----:|------|
| 주문 ID | orderId | Long | ✅ | |
| 별점 | rating | Integer | ✅ | 1 ~ 5 |
| 리뷰 내용 | content | String | ✅ | 최대 500자 |

**Response** `ReviewResponse`

| 설명 | 필드 | 타입 |
|------|------|------|
| 리뷰 ID | id | Long |
| 작성자 ID | userId | Long |
| 식당 ID | restaurantId | Long |
| 주문 ID | orderId | Long |
| 별점 | rating | int |
| 리뷰 내용 | content | String |
| 작성일시 | createdAt | LocalDateTime |

---

### PUT `/api/reviews/{reviewId}` — 리뷰 수정

**인증:** 필요

**Path Variable**

| 설명 | 파라미터 | 타입 |
|------|----------|------|
| 리뷰 ID | reviewId | Long |

**Request Body**

| 설명 | 필드 | 타입 | 필수 | 제약 |
|------|------|------|:----:|------|
| 별점 | rating | Integer | ✅ | 1 ~ 5 |
| 리뷰 내용 | content | String | ✅ | 최대 500자 |

**Response** `ReviewResponse` (위와 동일)

---

### DELETE `/api/reviews/{reviewId}` — 리뷰 삭제

**인증:** 필요

**Path Variable**

| 설명 | 파라미터 | 타입 |
|------|----------|------|
| 리뷰 ID | reviewId | Long |

**Response:** 없음

---

### GET `/api/restaurants/{restaurantId}/reviews` — 식당 리뷰 목록 조회

**인증:** 불필요

**Path Variable**

| 설명 | 파라미터 | 타입 |
|------|----------|------|
| 식당 ID | restaurantId | Long |

**Response** `List<ReviewResponse>` (위와 동일)

---

### GET `/api/reviews/me` — 내 리뷰 목록 조회

**인증:** 필요

Request 파라미터 없음

**Response** `List<ReviewResponse>` (위와 동일)
