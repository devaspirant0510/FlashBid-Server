# ⚙️ FlashBid-Server

> 실시간 경매 플랫폼의 백엔드 서버



## 목차
- 소개 
- 주요 기능
- 기술 스택
- 빠른 시작 (개발 환경)
  - 요구사항
  - 환경 변수 (.env 예시)
  - 로컬 실행
- Redis 설정
- Kafka / Redpanda (개발용)
- 아키텍처 
- Git 커밋 컨벤션


## 소개
FlashBid-Server는 실시간 경매 기능을 제공하는 백엔드 서비스입니다. WebSocket을 통한 실시간 입찰, Redis 기반 캐싱/세션 관리, Kafka(Redpanda)를 통한 이벤트 스트리밍을 주요 특징으로 합니다.


## 주요 기능
- 실시간 입찰(웹소켓)
- 입찰 이벤트 스트리밍(Kafka/Redpanda)
- Redis 기반 캐시 및 만료(입찰 종료 트리거 등)
- 사용자 인증(JWT/OAuth) 및 파일 업로드(S3)


## 기술 스택
| 기술 | 설명 | 비고 |
|---|---|---|
| Java | 주요 언어 | 17 |
| Spring Boot | 애플리케이션 프레임워크 | 3.x |
| Gradle | 빌드 및 의존성 관리 | 8.x |
| PostgreSQL | 관계형 DB | 16.x |
| Redis | 캐시 및 Pub/Sub | 7.x |
| Redpanda (Kafka 호환) | 메시징 큐 | Kafka 호환 |


## 빠른 시작 (개발 환경)
### 요구사항
- Java 17
- Gradle
- Docker (개발 시 Redis / Redpanda 실행에 편리)
- PostgreSQL (또는 로컬에서 실행 가능한 DB)


### 환경 변수 (.env 예시)

```env
# Database
DB_URL=jdbc:postgresql://localhost:5432/flash_bid
USERNAME=
PASSWORD=
MODE=dev

# JWT & OAuth
JWT_SECRET_KEY=3b4e
KAKAO_REST_API_KEY=
GOOGLE_CLIENT_ID=
GOOGLE_SECRET_KEY=
NAVER_CLIENT_ID=
NAVER_SECRET_KEY=

# AWS
AWS_S3_BUCKET=
AWS_ACCESS_KEY=
AWS_SECRET_KEY=
AWS_CLOUDFRONT_DOMAIN=

# Gmail SMTP
GMAIL_USERNAME=
GMAIL_PASSWORD=

# Notifications
DISCORD_WEBHOOK_URL=
```


### 로컬 실행 (예시)
1) 의존성 및 빌드

```bash
./gradlew clean build
```

2) 환경에 맞게 DB/Redis/Redpanda를 띄우세요(예: Docker 사용)

3) 애플리케이션 실행

```bash
./gradlew bootRun
```


## Redis 설정
개발 중 Redis CLI로 접속 후 keyspace 알림을 활성화하면 만료 이벤트 기반 로직(입찰 만료 등)을 처리할 수 있습니다.

```bash
redis-cli
# 서버에서 즉시 적용
config set notify-keyspace-events Ex
```

- 설명: `Ex`는 만료(expire) 이벤트에 대한 알림을 활성화합니다. 이 설정을 통해 Redis key 만료 시 애플리케이션이 적절히 반응하도록 구성할 수 있습니다.


## Kafka (Redpanda) 설정 — 개발용
Redpanda는 Kafka API 호환 구현으로 개발용으로 빠르게 사용할 수 있습니다.

```bash
# 간단히 로컬 Redpanda 실행 (개발용)
docker run -d --name redpanda -p 9092:9092 redpandadata/redpanda
```


## 아키텍처 

<img width="2346" height="1704" alt="제목 없는 다이어그램 drawio" src="https://github.com/user-attachments/assets/7841978d-0b39-4d94-8186-c7ad519b80a3" />

## Git 커밋 컨벤션
프론트/백 모두 일관된 커밋 규칙을 사용하세요. 예시:

| Prefix | 설명 | 예시 |
|---|---|---|
| feat | 새로운 기능 추가 | feat: Redis pub/sub 기반 만료 이벤트 추가 |
| fix | 버그 수정 | fix: Kafka producer 설정 버그 수정 |
| refactor | 코드 구조 개선 | refactor: service 레이어 구조 리팩토링 |
| chore | 설정/빌드 변경 | chore: Dockerfile 최적화 |
| docs | 문서 변경 | docs: README.md 백엔드 설정 추가 |



---
