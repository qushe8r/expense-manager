# expense-manager

<img width="1163" alt="스크린샷 2023-11-16 오전 10 58 00" src="https://github.com/qushe8r/expense-manager/assets/115606959/c9d3170b-e971-4b63-8595-92f1bcd0be57">

- 본 서비스는 사용자들이 개인 재무를 관리하고 지출을 추적하는 데 도움을 주는 애플리케이션입니다.
- 이 앱은 사용자들이 예산을 설정하고 지출을 모니터링하며 재무 목표를 달성하는 데 도움이 됩니다.


## 유저 스토리
[//]: # (이미지)
- 유저는 본 사이트에 들어와 회원가입을 통해 서비스를 이용합니다
- 예산 설정 및 설계 서비스
  - `월별` 총 예산을 설정합니다.
  - 본 서비스는 `카테고리` 별 예산을 설계(=추천)하여 사용자의 과다 지출을 방지합니다.
- 지출 기록
  - 사용자는 `지출`을 `금액`, `카테고리` 등을 지정하여 등록 합니다.
  - 언제든지 수정 및 삭제 할 수 있습니다.
- 지출 컨설팅
  - `월별` 설정한 예산을 기준으로 오늘 소비 가능한 `지출`을 `카테고리`별로 알려줍니다.
  
[//]: # (    IMG)
  - 매일 발생한 `지출`을 `카테고리` 별로 안내받습니다.

## 배포 아키텍처

<img width="946" alt="스크린샷 2023-11-22 오후 9 18 32" src="https://github.com/qushe8r/expense-manager/assets/115606959/81bb65f4-bd6d-416b-8316-0de1b6036f6e">

- NAT는 Application에서 나가는 요청을 담당하고 있습니다.
  - 나가는 요청을 따로 표시하면 혼잡해지기 때문에 요청에 대한 처리만 표시하였습니다.
- Nginx 설정 ( Load Balance, Certbot )
  <details>
  <summary>Nginx 설정 이미지 보기</summary>
  
  <img width="885" alt="스크린샷 2023-12-02 오후 11 04 05" src="https://github.com/qushe8r/expense-manager/assets/115606959/552768d9-f06b-43b6-9d20-f0e56809cd74">
  </details>
  

## 실행 방법

```
docker run --name expense-manager-db -e MYSQL_DATABASE=mysql-db -e MYSQL_USER=expense -e MYSQL_PASSWORD=manager -e MYSQL_ROOT_PASSWORD=1234 -d -p 3306:3306 mysql
```

```
docker run --name expense-manager-redis -d -p 6379:6379 redis
```

```
./script/build.sh
```
- 스크립트 실행시 실행되고 있는 `expense-manager` 를 종료합니다.
- 실행되고 있는 `expense-manager`를 종료 후에 테스트를 진행하기 위해 도커로 `33306`, `43306` 포트로 `MySQL`이 실행되고, `36379` 포트로 `Redis`가 실행됩니다.
- 빌드 후에 테스트용으로 사용한 도커를 종료하고 `container`를 제거합니다.
- `container` 제거 후에 빌드된 `.jar`를 실행합니다.

## API 명세서
- 위 스크립트 실행후 http://localhost:8080/index.html 에서 볼 수 있습니다.

## ERD

<img width="1461" alt="스크린샷 2023-11-22 오후 9 44 23" src="https://github.com/qushe8r/expense-manager/assets/115606959/e0c9d8d9-d25c-478a-83c7-7ac526ff0114">

## 알림 서비스를 위한 회원의 예산, 지출 조회 성능 개선
- 속도 개선을 테스트 하기 위해 유저를 500명, 지출 데이터를 1000만건 입력 후 성능 테스트 진행하였습니다.
- 쿼리에서 `Budget`이 `MemberCategoryId`의 `FK Index`만 적용되어 느렸던 쿼리를 검색조건인 `expenseAt`과 함께 `복합 인덱스`로 성능 개선 하였습니다.
  - 1200% 성능 개선 ( 2s 191ms --> 164ms )
- 개선 전 ( 2s 191ms )
  <img width="965" alt="스크린샷 2023-12-02 오후 8 13 35" src="https://github.com/qushe8r/expense-manager/assets/115606959/06b2ff8a-56cb-4e4a-911b-c736e08bff30">

[//]: # (  <details>)

[//]: # (  <summary>개선 전 쿼리 실행 속도 확인하기 - 이미지</summary>)

[//]: # ()
[//]: # (  <img width="965" alt="스크린샷 2023-12-02 오후 8 13 35" src="https://github.com/qushe8r/expense-manager/assets/115606959/06b2ff8a-56cb-4e4a-911b-c736e08bff30">)

[//]: # (  </details>)
  
- 개선 후 ( 164ms )
  <img width="965" alt="스크린샷 2023-12-02 오후 2 56 50 복사본" src="https://github.com/qushe8r/expense-manager/assets/115606959/850fa9ae-d9b6-415f-87e7-056f8bb54890">

[//]: # (  <details>)

[//]: # (  <summary>개선 후 쿼리 실행 속도 확인하기 - 이미지</summary>)

[//]: # ()
[//]: # (  <img width="965" alt="스크린샷 2023-12-02 오후 2 56 50 복사본" src="https://github.com/qushe8r/expense-manager/assets/115606959/850fa9ae-d9b6-415f-87e7-056f8bb54890">)

[//]: # (  </details>)

## TEST
### 커버리지 100%
  <img width="478" alt="스크린샷 2023-11-22 오후 9 45 46" src="https://github.com/qushe8r/expense-manager/assets/115606959/d740a0ab-aed8-4713-bfd4-183f43c2a391">

###  테스트 최적화
- Read/Write 구분
  - 테스트를 위해 매번 테스트에서 데이터를 준비하는 과정을 생략할 수 있습니다.
- Read Database 분리
  - 데이터 베이스 준비를 반복해서 하지 않기 위해서 Read Database를 분리 했습니다.
- Write Data Base 기본 구성
  - 테스트에서 `@Transactional`을 사용하면 테스트 전체에 트랜잭션이 걸려서 실제 동작과 다르게 작동할 수 있기 때문에 Write Database를 분리 했습니다.

### 빌드 시간 개선
- 반복되는 테스트의 실행시간을 줄이려고 노력하였습니다.
- 테스트 병렬 실행으로 테스트 실행 시간이 줄어들어 빌드 시간이 개선 되었습니다.
  - 145% 속도 개선 ( 29s --> 20s )
- 개선 전
  <img width="1642" alt="스크린샷 2023-12-02 오후 9 05 54 복사본" src="https://github.com/qushe8r/expense-manager/assets/115606959/2d21cde2-67e8-451e-b3b5-1325bd4d2e78">

[//]: # (  <details>)

[//]: # (  <summary>개선 전 빌드 시간 확인하기 - 이미지</summary>)

[//]: # ()
[//]: # (  <img width="1642" alt="스크린샷 2023-12-02 오후 9 05 54 복사본" src="https://github.com/qushe8r/expense-manager/assets/115606959/2d21cde2-67e8-451e-b3b5-1325bd4d2e78">)

[//]: # (  </details>)

- 개선 후
  <img width="1642" alt="스크린샷 2023-12-02 오후 9 50 54 복사본" src="https://github.com/qushe8r/expense-manager/assets/115606959/8697c636-9175-49f5-bb3d-0c8761db8b60">

[//]: # (  <details>)

[//]: # (  <summary>개선 전 빌드 시간 확인하기 - 이미지</summary>)

[//]: # ()
[//]: # (  <img width="1642" alt="스크린샷 2023-12-02 오후 9 50 54 복사본" src="https://github.com/qushe8r/expense-manager/assets/115606959/8697c636-9175-49f5-bb3d-0c8761db8b60">)

[//]: # (  </details>)

### 테스트 의존성 사용
- await
  - 비동기적으로 일어나는 일을 지속적으로 상태 확인 후 상태가 정상적으로 변한다면 테스트에 성공하게 됩니다.
  - Redis의 TTL 테스트에 적용하였습니다.
- WebClient
  - 비동기적으로 webhook을 보내게 되는데 보내는 주소를 MockServer로 적용하여 MockServer에서 요청을 받는다면 성공합니다.
  - Discord Event Linstener Test에 적용하였습니다.

### SpringSecurity 테스트 환경 구성
  - `@WebMvcTestWithoutSecurityConfig`
    - Security가 필요 없는 테스트(ex: 회원가입 등)에서는 `SpringSecurity`의 기본 설정로드를 막습니다. 
  - `@WithMemberPrincipals`
    - 어노테이션을 적용한 테스트에서는 `WithMemberPrincipalsSecurityContextFactory`에서 `SecurityContextHolder`에 `MemberDetails`가 포함된 가짜 `Authentication`을 넣어줍니다. 
  - `TestSecurityConfig`
    - `MemberDetails`를 적용하였기 때문에 테스트에서 `SecurityContextHolder`에 `Authentication`이 있어야 합니다.
    - `TestSecurityConfig`를 적용한 테스트에 `@WithMemberPrincipals`를 적용하여 테스트 하였습니다.

### ArgumentMatcher
  - `Mockito`를 사용하여 mock 테스트를 진행할때 `mock의 입력값`에 대해서도 검증 할 수 있습니다.
  - 입력값을 검증하지 않는다면 입력값이 다를 경우에도 테스트가 성공하기 때문에 반쪽짜리 테스트가 됩니다.

[//]: # (  - &#40;이미지 추가&#41;)

[//]: # (설명 이미지)

## 인증 & 인가 (Spring Security)

### 로그인: JwtAuthenticationFilter
- `id`와 `password`로 인증을 정상적으로 완료하면 `액세스 토큰`을 `Authorization 헤더`로, `리프레시 토큰`은 `쿠키`로 응답해줍니다.

### 인증: JwtVerificationFilter
- `Authorization 헤더`에 들어오는 `액세스 토큰`을 해석하여 정상적으로 해석되면 목표 `url`에 접근할 수 있습니다.
- Authorization 헤더에 들어온 토큰이 `정상적이이 않은 경우` 토큰이 정상적이지 않다는 응답을 내려줍니다.

### 인증된 유저 객체: MemberDetails
- 위의 두 인증과정에서 각각 `JwtAuthenticationFilter`에서는 `조회된 유저`로, `JwtVerificationFilter`에서는 `토큰에 있는 인증정보`로 각각 인증된 유저 객체를 생성합니다.
- 인증된 유저는 `@AuthenticationPrincipal` 어노테이션이 있으면 `Controller`에서 `ArgumentResolver`를 통해 `MemberDetails` 타입으로 받을 수 있습니다.
- 인증된 유저 타입을 `MemberDetails`로 `통일` 시킴으로써 컨트롤러에서 인증된 유저라면 `MemberDetails` 타입으로 `부담없이` 꺼내 쓸 수 있습니다.

### 인가: SecurityConfig
- `SecurityConfig`에서 각 URL에 대해서 필요한 권한을 설정합니다.
- 위에서 인증된 권한이 접근 가능한 권한이라면 목표 `url` 에 접근할 수 있습니다.

## Lint: Spotless
- Google Java Format
  - 장점:
    - 협업을 한다고 가정했을 때, 다른사람의 코드 포맷에 대해서 이야기할필요가 없다.
    - 횡스크롤이 없어서 코드 볼때 편하다.
    - 린트를 적용함으로서 코드 포매팅이 되지 않았다면 빌드가 되지 않는다.
  - 단점:
    - lambda 코드에서 엔터가 자주 발생한다.
    - 린트를 적용함으로서 코드 포매팅이 되지 않았다면 빌드가 되지 않는다.
