# 🛵 Delivery Service

> Spring Boot 기반 배달/주문 서비스 토이프로젝트  
> Jenkins CI/CD + Docker + NGINX + Redis + MySQL Replication + Vault + Firebase 구성

---

## 📌 프로젝트 소개

배달 플랫폼을 직접 구현하며 실무 수준의 인프라 아키텍처를 경험하는 것을 목표로 합니다.  
단순 기능 구현을 넘어 **CI/CD 자동화**, **서버 다중화**, **DB 이중화**, **보안 관리**까지 포함한 풀스택 인프라를 다룹니다.

---

## 🏗️ 아키텍처

```
User → NGINX (Load Balancer) → Spring Server × 3
                                     ↓
                          MySQL Master / Slave
                          Redis Session / Cache
                               ↓
                           Firebase (Rider 푸시 알림)

Developer → GitHub → Jenkins → Docker → Deploy
                ↕
           Vault Server (시크릿 관리)
```

---

## 🛠️ 기술 스택

| 분류 | 기술 |
|---|---|
| Backend | Spring Boot 3.x, Spring Security, JPA |
| Database | MySQL 8.0 (Master/Slave) |
| Cache | Redis (Session / Cache 분리) |
| CI/CD | Jenkins, GitHub Webhook |
| Infra | Docker, Docker Compose, NGINX |
| 보안 | HashiCorp Vault, JWT |
| 알림 | Firebase Cloud Messaging (FCM) |
| 결제 | Portone (아임포트) |
| 문서 | Swagger (SpringDoc) |

---

## 👥 도메인

| 도메인 | 설명 |
|---|---|
| User | 일반 고객 — 회원가입, 로그인, 주문 |
| Owner | 음식점 사장님 — 음식점/메뉴 관리, 주문 수락 |
| Rider | 배달원 — 배달 수행, FCM 알림 수신 |
| Restaurant | 음식점 정보 및 메뉴 관리 |
| Order | 주문 생성 및 상태 관리 |
| Payment | Portone 연동 결제 |
| Review | 주문 완료 후 리뷰/평점 |

---

## 📁 프로젝트 구조

```
delivery-project/
│
├── docs/
│   ├── 01-architecture/     # 아키텍처 설명
│   ├── 02-database/         # ERD 및 테이블 명세
│   ├── 03-api/              # API 명세서
│   ├── 04-infra/            # 인프라 구성 문서
│   └── 05-convention/       # 브랜치 전략, 코드 컨벤션
│
├── src/                     # Spring Boot 소스코드
├── docker-compose.yml
├── Dockerfile
├── Jenkinsfile
└── README.md
```

---

## 🗺️ 개발 로드맵

- [x] Phase 1 — 프로젝트 설계 (ERD, API 명세, 컨벤션)
- [ ] Phase 2 — Spring Boot 앱 개발 (CRUD, JWT 인증)
- [ ] Phase 3 — Docker 컨테이너화
- [ ] Phase 4 — Redis 도입 (Session / Cache)
- [ ] Phase 5 — Jenkins CI/CD 파이프라인
- [ ] Phase 6 — NGINX + 서버 다중화
- [ ] Phase 7 — MySQL Master/Slave + Vault + Firebase

---

## 📄 문서

- [아키텍처 설명](./docs/01-architecture/architecture.md)
- [ERD](./docs/02-database/erd.mermaid)
- [API 명세서](./docs/03-api/api-spec.md)
- [Git 컨벤션](./docs/05-convention/git-convention.md)
- [Docker 구성](./docs/04-infra/docker.md)
- [Jenkins 파이프라인](./docs/04-infra/jenkins.md)

---

## ⚙️ 로컬 실행 방법

```bash
# 전체 컨테이너 실행
docker-compose up -d

# 앱만 실행 (개발 시)
./gradlew bootRun
```

> 환경변수 설정은 [Vault 문서](./docs/04-infra/vault.md) 참고

---

## 📝 License

MIT
