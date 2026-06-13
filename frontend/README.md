# Realtime Chat Frontend

Vue 3 + Vite SPA. 현재 백엔드(`realtime-chat-platform`, `:8080`)의 인증/회원 API에 연동됩니다.

## 사전 요구
- Node.js 18+ (LTS 권장)
- 백엔드가 `:8080`에서 실행 중 (`./gradlew bootRun`), MySQL/Redis 도커 기동

## 실행
```bash
cd frontend
npm install
npm run dev
```
→ http://localhost:3000 (백엔드 CORS가 이 origin을 허용하도록 설정돼 있음)

## 환경변수
`.env`
```
VITE_API_BASE_URL=http://localhost:8080
```

## 화면 / 흐름
- `/sign-up` 회원가입 → `POST /member/sign-up`
- `/sign-in` 로그인 → `POST /member/sign-in`, 받은 `token`을 `localStorage`에 저장
- `/members` 회원목록 → `GET /member/list` (axios 인터셉터가 `Authorization: Bearer <token>` 자동 첨부)

## 구조
```
src/
  api/
    client.js   axios 인스턴스 + 토큰 저장(localStorage) + Bearer 인터셉터
    auth.js     signUp / signIn
    member.js   fetchMembers
  router/index.js  라우팅 + 인증 가드(requiresAuth)
  views/      SignUp / SignIn / Members
  App.vue     내비게이션 + 로그아웃
```
