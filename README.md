# expense-manager

<img width="1163" alt="스크린샷 2023-11-16 오전 10 58 00" src="https://github.com/qushe8r/expense-manager/assets/115606959/c9d3170b-e971-4b63-8595-92f1bcd0be57">

- 본 서비스는 사용자들이 개인 재무를 관리하고 지출을 추적하는 데 도움을 주는 애플리케이션입니다.
- 이 앱은 사용자들이 예산을 설정하고 지출을 모니터링하며 재무 목표를 달성하는 데 도움이 됩니다.

### 추가 개발 계획:
- ㅇ/ㅇㅇㅇ 까지 ㅇㅇㅇ
- ㅇ/ㅇㅇㅇ 까지 ㅇㅇㅇ

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

## 실행 방법

- 테스트 컨테이너 적용 이후 업데이트 될 예정입니다.

## API 명세서

- RestDocs 작성 후 업데이트 될 예정입니다.

- openapi.yml 작성 후 업데이트 될 예정입니다.
- swaggerhub에 업로드 후 업데이트 될 예정입니다.

## ERD

<img width="1461" alt="스크린샷 2023-11-22 오후 9 44 23" src="https://github.com/qushe8r/expense-manager/assets/115606959/e0c9d8d9-d25c-478a-83c7-7ac526ff0114">

## TEST
- 커버리지 100%
  <img width="478" alt="스크린샷 2023-11-22 오후 9 45 46" src="https://github.com/qushe8r/expense-manager/assets/115606959/d740a0ab-aed8-4713-bfd4-183f43c2a391">
- 테스트 최적화
  - Read/Write 구분
  - Read Data Base 기본 구성
  - Write Data Base 기본 구성
- 테스트 의존성 사용
  - await
    - 비동기적으로 일어나는 일을 지속적으로 상태 확인 후 상태가 정상적으로 변한다면 테스트에 성공하게 됩니다.
    - Redis의 TTL 테스트에 적용하였습니다.
  - WebClient
    - 비동기적으로 webhook을 보내게 되는데 보내는 주소를 MockServer로 적용하여 MockServer에서 요청을 받는다면 성공합니다.
    - Discord Event Linstener Test에 적용하였습니다.
- SpringSecurity 테스트 환경 구성
  - `@WebMvcTestWithoutSecurityConfig`
    - Security가 필요 없는 테스트(ex: 회원가입 등)에서는 `SpringSecurity`의 기본 설정로드를 막습니다. 
  - `@WithMemberPrincipals`
    - 어노테이션을 적용한 테스트에서는 `WithMemberPrincipalsSecurityContextFactory`에서 `SecurityContextHolder`에 `MemberDetails`가 포함된 가짜 `Authentication`을 넣어줍니다. 
  - `TestSecurityConfig`
    - `MemberDetails`를 적용하였기 때문에 테스트에서 `SecurityContextHolder`에 `Authentication`이 있어야 합니다.
    - `TestSecurityConfig`를 적용한 테스트에 `@WithMemberPrincipals`를 적용하여 테스트 하였습니다.
- ArgumentMatcher
  - Mockito를 사용하여 mock 테스트를 진행할때 mock의 입력값에 대해서도 검증 할 수 있습니다.
  - 입력값을 검증하지 않는다면 입력값이 다를 경우에도 테스트가 성공하기 때문에 반쪽짜리 테스트가 됩니다.

[//]: # (설명 이미지)

## Spring Security

[//]: # (필터 다이어그램)

## Lint: Spotless
- Google Java Format
  - 장점:
    - 협업을 한다고 가정했을 때, 다른사람의 코드 포맷에 대해서 이야기할필요가 없다.
    - 횡스크롤이 없어서 코드 볼때 편하다.
    - 린트를 적용함으로서 코드 포매팅이 되지 않았다면 빌드가 되지 않는다.
  - 단점:
    - lambda 코드에서 엔터가 자주 발생한다.
    - 린트를 적용함으로서 코드 포매팅이 되지 않았다면 빌드가 되지 않는다.
