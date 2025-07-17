# PAWKEY-Server

**반려동물과 보호자의 산책 경험을 더 풍부하게 만들어주는, 위치 기반 산책 큐레이션 플랫폼의 백엔드 서버**

> 36th AT SOPT - PAWKEY 백엔드 레포지토리  
> 📅 2025.06.26 ~ ing

<img src ="https://github.com/user-attachments/assets/a83be8cb-5cf4-4309-99f6-c4b10ea23fd7"/>

## 📍 주요 기능

### 1. 지역 기반 추천 루트 API

- 유저의 현재 위치(좌표)와 선호조건(풍경, 카페 등)에 따라 인기 산책 루트 추천 API 제공
- 다양한 산책 코스 데이터 제공 및 ‘좋아요’/‘저장’ 기능 지원


### 2. 산책 루트 기록 및 리뷰 관리

- GPS 기반 산책 기록 데이터 관리 (기록 시작/종료, 경로 저장)
- 산책 리뷰 작성 및 체크리스트(안전/경치/편의성) 제출 기능
- 산책 루트의 공개/비공개 아카이빙 지원


### 3. 실시간 친구 산책 연결

- 친구 추가/관리 기능 (소셜 여부 설정 포함)
- 실시간 산책 상태 조회 및 친구와 동반 산책 제안 관리


## 🖥️ Contributors

| [한다은](https://github.com/daeun-han) | [👸이수민](https://github.com/dltnals317) | [한성재](https://github.com/bingle625) |
|:------------------------------------:|:-------------------------:|:------------------------------:|
| <img width="210" alt="image" src="https://github.com/user-attachments/assets/1f717cd5-b788-40c2-b7fb-2c2e030079aa" /> | <img width="210" alt="image" src="https://github.com/user-attachments/assets/272b582e-d441-4045-b0bf-88e2c5609c4b" /> | <img width="210" alt="image" src="https://github.com/user-attachments/assets/20c1c40d-21a8-4a58-91c7-4c635556d7b9" /> |
|`검색 필터링 로직`| `인프라`  | `코드 품질 관리` |

## 🛠️ Tech Stack

| **Category** | **Content** |
| :-- | :-- |
| **Language** | Java 등 |
| **Framework** | Spring Boot |
| **DB** | PostgreSQL(Spatial), Redis(캐싱/세션) |
| **API 문서화** | Swagger, OpenAPI |
| **CI/CD** | GitHub Actions, Docker, DockerHub |
| **기타** | JPA/QueryDSL, S3(사진업로드), GeoJSON |

## 💡 Convention
#### 🐾 Git Convention
[Git Convention](https://shadow-impatiens-f13.notion.site/Git-Code-215564d8d2a780f186e3f562dc687a2f?source=copy_link)

#### 📂 Package Convention
[Package Convention](https://shadow-impatiens-f13.notion.site/md-221564d8d2a780ab9235e9f9ea062b03?source=copy_link)

## 🎨 설계
### 서버 아키텍쳐
<img width="2048" height="1012" alt="image" src="https://github.com/user-attachments/assets/4047dc54-a2f8-4407-a2f6-5ba7899b40b8" />

| **기능 영역**         | **선택한 기술 스택**                | **선택 이유 (근거)**                                                                 |
|----------------------|------------------------------------|--------------------------------------------------------------------------------------|
| **API 서버**         | Spring Boot (Java 17)             | - 자바 기반의 강력한 타입 안정성<br>- 대규모 트래픽 처리 검증 (기업 표준)<br>- Spring Security, JPA 등 통합된 생태계 |
| **DB**               | PostgreSQL (PostGIS 포함)         | - ACID 보장, 대규모 데이터에서의 안정성<br>- 복잡한 지리 좌표 데이터(Polygon, LineString) 처리 가능   |
| **파일 스토리지**    | AWS S3                            | - 대용량 이미지 저장 및 CDN 연동 용이<br>- 서버 스토리지와 분리되어 무중단 운영 가능                   |
| **CI/CD**            | GitHub Actions + Docker + EC2     | - PR/merge 시 자동 빌드 & 배포<br>- Docker로 환경 일관성 유지                                          |
| **HTTPS & 리버스 프록시** | Nginx + Certbot (Let’s Encrypt) | - SSL 인증서 자동 갱신<br>- 정적 파일 서빙 및 부하 분산                                                |
| **API 문서화**       | Swagger (OpenAPI 3.0)             | - 자동화된 API 문서 생성<br>- 팀원 및 외부 개발자와 연동 편의성 제공                                    |

## 계층 구조
<img width="2001" height="831" alt="image" src="https://github.com/user-attachments/assets/3eacdc8d-614f-4c4e-9f1a-ff26ffd193aa" />

```markdown
org.sopt.pawkey.backendapi
├── domain                           # 📦 도메인별 모듈
│   └── user                         # 👤 사용자 도메인
│       ├── api                      # 🌐 프레젠테이션 계층
│       │   ├── controller/          # REST API 컨트롤러
│       │   └── dto/                 # API 요청/응답 DTO
│       │       ├── request/
│       │       └── response/
│       ├── application              # 🔧 애플리케이션 계층
│       │   ├── dto/                 # 서비스 계층 DTO
│       │   │   ├── request/
│       │   │   └── response/
│       │   ├── facade/              # 파사드 (유스케이스 조합)
│       │   └── service/             # 비즈니스 서비스
│       ├── domain                   # 🎯 도메인 모델 계층
│       │   └── repository/          # 저장소 인터페이스
│       ├── exception                # ❌ 도메인별 예외
│       └── infra                    # 🔌 인프라스트럭처 계층
│           └── persistence/
│               ├── entity/          # JPA 엔티티
│               └── repository/      # 저장소 구현체
└── global                           # 🌍 전역 공통 모듈
    ├── config/                      # 설정 클래스
    ├── exception/                   # 공통 예외 처리
    │   └── handler/
    ├── infra/                       # 공통 인프라
    │   └── persistence/entity/      # BaseEntity
    └── response/                    # 공통 응답 포맷
```

1. **도메인 중심 설계 (Domain-Centric)**
    - **비즈니스 로직**은 `domain.model` 패키지에 집중
    - **도메인 규칙**은 도메인 엔티티 생성자에서 검증
    - **영속성 기술**과 도메인 로직 완전 분리
2. **의존성 역전 원칙 (DIP) 적용**
    
    ```java
    // ✅ 올바른 의존성 방향
    domain.repository.UserRepository (인터페이스)
        ⬆️ 의존
    infra.persistence.UserRepositoryImpl (구현체)
    ```
    
3. **계층별 DTO 분리**
    - **API DTO**: 클라이언트와의 통신용 (`api.dto`)
    - **application DTO:** application 계층 내부 통신용 (`application.dto`)
    - **Domain Model:** 순수 비즈니스 객체 (`domain.model`)
4. **단일 책임 원칙**
    - **Controller:** HTTP 요청/응답 처리만
    - **Facade:** 여러 서비스 조합 및 트랜잭션 관리
    - **Service:** 단일 도메인 비즈니스 로직
    - **Repository**: 데이터 접근만

