# Realtime Chat Platform

실시간 채팅 플랫폼. 저장소는 백엔드와 프론트엔드로 구성됩니다.

```
.
├── backend/    Spring Boot 4 + Kotlin (REST API, JWT 인증, MySQL/Redis)
└── frontend/   Vue 3 + Vite SPA
```

## 사전 요구

- JDK 17
- Node.js 18+ (LTS 권장)
- Docker / Docker Compose (MySQL · Redis 기동용)

---

## Backend (`backend/`)

Spring Boot 4 + Kotlin. `:8080`에서 동작하며 인증/회원 REST API를 제공합니다.

### 1. 인프라 기동 (MySQL · Redis)

```bash
cd backend
cp .env.example .env   # 필요 시 값 수정
docker compose up -d
```

`docker-compose.yml`이 MySQL 8.4(`:3306`)와 Redis 7.4(`:6379`)를 띄웁니다.

### 2. 애플리케이션 실행

```bash
cd backend
./gradlew bootRun
```

→ http://localhost:8080

빌드 산출물(jar)로 실행하려면:

```bash
cd backend
./gradlew build
java -jar build/libs/realtime-chat-platform-0.0.1-SNAPSHOT.jar
```

### 환경변수

`application.yaml`은 아래 값을 환경변수로 오버라이드할 수 있습니다(괄호는 기본값).

| 변수 | 설명 | 기본값 |
| --- | --- | --- |
| `MYSQL_HOST` / `MYSQL_PORT` | MySQL 접속 | `localhost` / `3306` |
| `MYSQL_DATABASE` | DB 이름 | `rtchat` |
| `MYSQL_USER` / `MYSQL_PASSWORD` | DB 계정 | `rtchat` / `rtchatpassword` |
| `REDIS_HOST` / `REDIS_PORT` | Redis 접속 | `localhost` / `6379` |
| `JWT_SECRET_KEY` | JWT 서명 키 | (개발용 기본값 내장) |
| `JWT_EXPIRATION` | 토큰 만료(ms) | `3000` |

> MySQL/Redis 관련 기본값은 `backend/.env.example` 및 `docker-compose.yml`과 동일합니다.

### 주요 API

| 메서드 | 경로 | 설명 |
| --- | --- | --- |
| `POST` | `/member/sign-up` | 회원가입 |
| `POST` | `/member/sign-in` | 로그인 → JWT 토큰 발급 |
| `GET` | `/member/list` | 회원 목록 (인증 필요, `Authorization: Bearer <token>`) |

CORS는 `http://localhost:3000`(프론트엔드 dev 서버)를 허용합니다.

---

## Frontend (`frontend/`)

Vue 3 + Vite SPA. `:3000`에서 동작하며 백엔드(`:8080`) API에 연동됩니다.

### 실행

```bash
cd frontend
npm install
npm run dev
```

→ http://localhost:3000

프로덕션 빌드 / 미리보기:

```bash
cd frontend
npm run build     # dist/ 생성
npm run preview
```

### 환경변수

`frontend/.env`

```
VITE_API_BASE_URL=http://localhost:8080
```

### 화면 / 흐름

- `/sign-up` 회원가입 → `POST /member/sign-up`
- `/sign-in` 로그인 → `POST /member/sign-in`, 받은 `token`을 `localStorage`에 저장
- `/members` 회원목록 → `GET /member/list` (axios 인터셉터가 `Authorization: Bearer <token>` 자동 첨부)

더 자세한 내용은 [`frontend/README.md`](frontend/README.md) 참고.

---

## 빠른 시작 (전체)

```bash
# 1) 백엔드 인프라
cd backend && cp .env.example .env && docker compose up -d

# 2) 백엔드 (새 터미널)
cd backend && ./gradlew bootRun

# 3) 프론트엔드 (새 터미널)
cd frontend && npm install && npm run dev
```

이후 http://localhost:3000 접속.
