# Git 컨벤션

## 브랜치 전략

`Git Flow` 전략을 기반으로 합니다.

```
main
├── develop
│   ├── feat/user-auth
│   ├── feat/order-create
│   ├── feat/payment
│   └── feat/review
├── release/1.0.0
└── hotfix/fix-payment-bug
```

### 브랜치 종류

| 브랜치 | 용도 |
|---|---|
| `main` | 배포 가능한 최종 브랜치. 직접 push 금지 |
| `develop` | 개발 통합 브랜치. feat 브랜치의 merge 대상 |
| `feat/*` | 기능 단위 개발 브랜치 |
| `release/*` | 배포 전 QA 브랜치 |
| `hotfix/*` | 운영 긴급 버그 수정 |

### 브랜치 네이밍 규칙

```
feat/{도메인}-{작업내용}

예시)
feat/user-signup
feat/order-create
feat/payment-toss
feat/rider-fcm
feat/review-crud
```

---

## 커밋 컨벤션

### 형식

```
{타입}: {제목}

{본문 - 선택}

{꼬리말 - 선택}
```

### 커밋 타입

| 타입 | 설명 |
|---|---|
| `feat` | 새로운 기능 추가 |
| `fix` | 버그 수정 |
| `refactor` | 코드 리팩토링 (기능 변경 없음) |
| `docs` | 문서 수정 |
| `test` | 테스트 코드 추가/수정 |
| `chore` | 빌드, 설정 파일 수정 |
| `style` | 코드 포맷팅, 세미콜론 누락 등 |
| `perf` | 성능 개선 |
| `ci` | CI/CD 관련 수정 |
| `infra` | 인프라 관련 수정 |

### 커밋 예시

```
feat: 회원가입 API 구현

- 이메일 중복 체크 로직 추가
- 비밀번호 BCrypt 암호화 적용
- 가입 완료 시 JWT 토큰 발급

Closes #12
```

```
fix: 주문 생성 시 재고 차감 누락 버그 수정

Fixes #34
```

```
docs: ERD 테이블 명세 문서 추가
```

### 커밋 규칙

- 제목은 **50자 이내**, 마침표 없이 작성
- 제목은 **현재형 동사**로 시작 (`추가`, `수정`, `삭제`)
- 본문은 **무엇을, 왜** 변경했는지 작성
- 이슈 번호 연결 시 꼬리말에 `Closes #번호` 형식 사용

---

## PR 규칙

### PR 제목

```
[{타입}] {작업 내용}

예시)
[feat] 회원가입/로그인 API 구현
[fix] 결제 금액 검증 오류 수정
[infra] Jenkins 파이프라인 구성
```

### PR 템플릿

```markdown
## 작업 내용
- 

## 변경 사항
- 

## 테스트
- [ ] 단위 테스트 통과
- [ ] 통합 테스트 통과
- [ ] Postman으로 API 확인

## 관련 이슈
Closes #
```

### PR 규칙

- `feature` → `develop` 머지 시 PR 필수
- `develop` → `main` 머지 시 PR 필수
- Self-merge 지양 (혼자 진행 시 최소 24시간 후 머지)
- PR 단위는 **하나의 기능** 단위로 작게 유지

---

## 이슈 관리

### 이슈 라벨

| 라벨 | 설명 |
|---|---|
| `feature` | 기능 개발 |
| `bug` | 버그 |
| `docs` | 문서 |
| `infra` | 인프라 |
| `refactor` | 리팩토링 |

### 이슈 템플릿

```markdown
## 작업 목표


## 상세 내용


## 체크리스트
- [ ] 
```
