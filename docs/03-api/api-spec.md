# API 명세서 (API Specification)

> Postman 컬렉션을 기반으로 작성된 API 명세서입니다.
> 인증이 필요한 API는 `Authorization: Bearer {accessToken}` 헤더를 포함해야 합니다.

---

## 목차 (API Folder Structure)

- **1. 회원**
    - 1.1 회원가입 `POST /api/auth/signup`
    - 1.2 로그인 `POST /api/auth/login`
    - 1.3 내 정보 조회 `GET /api/users/me`
    - 1.4 내 정보 수정 `PUT /api/users/me`
- **2. 음식점**
    - 2.1 음식점 등록 `POST /api/owner/restaurants`
    - 2.2 음식점 목록 조회 `GET /api/restaurants`
    - 2.3 음식점 상세 조회 `GET /api/restaurants/{restaurantId}`
    - 2.4 음식점 수정 `PUT /api/owner/restaurants/{restaurantId}`
    - 2.5 음식점 영업 상태 변경 `PATCH /api/owner/restaurants/{restaurantId}/status`
    - 2.6 음식점 삭제 `DELETE /api/owner/restaurants/{restaurantId}`
- **3. 메뉴**
    - 3.1 메뉴 등록 `POST /api/owner/restaurants/{restaurantId}/menus`
    - 3.2 메뉴 목록 조회 `GET /api/restaurants/{restaurantId}/menus`
    - 3.3 메뉴 상태 변경 `PATCH /api/owner/menus/{menuId}/status`

---

## 1. 회원

### 1.1 회원가입
- **Method:** `POST`
- **URL:** `/api/auth/signup`
- **Auth 필요:** (X)
- **Request Body:**
  ```json
  {
      "email" : "owner@test.com",
      "password": "password123!",
      "name": "김사장",
      "phone": "010-1234-5678",
      "role": "OWNER"
  }
  ```

### 1.2 로그인
- **Method:** `POST`
- **URL:** `/api/auth/login`
- **Auth 필요:** (X)
- **Request Body:**
  ```json
  {
      "email": "owner@test.com",
      "password": "password123!"
  }
  ```

### 1.3 내 정보 조회
- **Method:** `GET`
- **URL:** `/api/users/me`
- **Auth 필요:** (O)

### 1.4 내 정보 수정
- **Method:** `PUT`
- **URL:** `/api/users/me`
- **Auth 필요:** (O)
- **Request Body:**
  ```json
  {
      "name": "김사장 수정",
      "phone": "010-9999-9999",
      "address": "제주도 서귀포시"
  }
  ```

---

## 2. 음식점

### 2.1 음식점 등록 (사장님)
- **Method:** `POST`
- **URL:** `/api/owner/restaurants`
- **Auth 필요:** (O) - OWNER 권한
- **Request Body:**
  ```json
  {
      "name": "BBQ 강남점",
      "address": "서울시 강남구",
      "phone": "02-1234-1234",
      "category": "CHICKEN",
      "deliveryFee": 2000,
      "minOrderPrice": 10000
  }
  ```

### 2.2 음식점 목록 조회
- **Method:** `GET`
- **URL:** `/api/restaurants`
- **Auth 필요:** (X)

### 2.3 음식점 상세 조회
- **Method:** `GET`
- **URL:** `/api/restaurants/{restaurantId}`
- **Auth 필요:** (X)

### 2.4 음식점 수정 (사장님)
- **Method:** `PUT`
- **URL:** `/api/owner/restaurants/{restaurantId}`
- **Auth 필요:** (O) - OWNER 권한
- **Request Body:**
  ```json
  {
      "name" : "BBQ 노원점",
      "address" : "서울 노원구 XXX",
      "phone": "02-258-2580",
      "category": "CHICKEN",
      "minOrderPrice": 1000,
      "deliveryFee": 2000
  }
  ```

### 2.5 음식점 영업 상태 변경 (사장님)
- **Method:** `PATCH`
- **URL:** `/api/owner/restaurants/{restaurantId}/status`
- **Auth 필요:** (O) - OWNER 권한
- **Request Body:**
  ```json
  {
      "status": "CLOSED" 
  }
  ```

### 2.6 음식점 삭제 (사장님)
- **Method:** `DELETE`
- **URL:** `/api/owner/restaurants/{restaurantId}`
- **Auth 필요:** (O) - OWNER 권한

---

## 3. 메뉴

### 3.1 메뉴 등록 (사장님)
- **Method:** `POST`
- **URL:** `/api/owner/restaurants/{restaurantId}/menus`
- **Auth 필요:** (O) - OWNER 권한
- **Request Body:**
  ```json
  {
      "name" : "양념 치킨",
      "price": 20000
  }
  ```

### 3.2 메뉴 목록 조회
- **Description:** 해당 음식점에 등록된 메뉴 목록을 조회한다.
- **Method:** `GET`
- **URL:** `/api/restaurants/{restaurantId}/menus`
- **Auth 필요:** (X)

### 3.3 메뉴 상태 변경 (사장님)
- **Method:** `PATCH`
- **URL:** `/api/owner/menus/{menuId}/status`
- **Auth 필요:** (O) - OWNER 권한
- **Request Body:**
  ```json
  {
      "status": "AVAILABLE"
  }
  ```