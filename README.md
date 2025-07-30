<img width="1920" height="1289" alt="Image" src="https://github.com/user-attachments/assets/8fb74b91-ec5e-4be5-bfad-418166a5a709" />

<br/>
<br/>

# 0. Getting Started (시작하기)
```bash
$ build.clean
$ build bootjar
$ docker compose -f docker-compose.monitoring.yaml up -d
$ docker compose -f docker-compose.data.yaml up -d
```
<br/>


[함께하개냥](http://2gaenyang.site/)

<br/>

# 1. Project Overview (프로젝트 개요)
- 프로젝트 이름: 함께하개냥
- 프로젝트 설명: 유기동물 입양 및 홍보 커뮤니티
  - 멋쟁이사자처럼 백엔드스쿨 플러스 Elasticsearch 기반의 고성능 검색 엔진을 설계 **최우수상**
<br/>

# 2. Team Members (팀원 및 팀 소개)
| 선순주 | 박세정 | 엄아영 | 이지은 | 오승훈 | 이은서 |
|:------:|:------:|:------:|:------:|:------:|:------:|
| BE |  BE | BE | BE | BE | BE |
|<img width="252" height="252" alt="Image" src="https://github.com/user-attachments/assets/98217954-b830-4f33-9a90-2ae967567ed9" /> |<<img width="252" height="252" alt="Image" src="https://github.com/user-attachments/assets/27eebd22-1686-4649-a708-2e81c5835313" />|<img width="252" height="252" alt="Image" src="https://github.com/user-attachments/assets/0a0b18ab-354d-4d73-89ab-b52b1a1778b5" /> |<img width="252" height="252" alt="Image" src="https://github.com/user-attachments/assets/1eb4cd62-2c8c-4455-a779-0654f6fe0bed" />|<img width="252" height="252" alt="Image" src="https://github.com/user-attachments/assets/03e418c7-e72c-41ad-83bf-314f4832b57c" /> | <img width="252" height="252" alt="Image" src="https://github.com/user-attachments/assets/024935dd-908b-4144-8083-e94eac8f1f83" /> |
| [GitHub](https://github.com/dev-ssj) | [GitHub](https://github.com/zoni613) | [GitHub](https://github.com/Eomssi) | [GitHub](https://github.com/Ljeelra) | [GitHub](https://github.com/os-hoon) | [GitHub](https://github.com/ieunseo) |

<br/>
<br/>

# 3. Key Features (주요 기능)

- **로그인**:
  - 소셜에서 제공하는 인증 정보를 통해 로그인합니다.
  - 회원가입 또한 최초로그인시 회원가입과 동시에 로그인이 진행됩니다.

- **유기동물전체보기**:
  - 공공기관 유기동물 API에서 공고시작일 기준 최근16일 보여줍니다.
  - 발견일을 기준으로 정렬합니다.
  
- **게시판**:
  - 입양후기게시판 : 우리 사이트를 통해서 가족이된 반려동물들을 소개하는 게시판
  - SNS 게시판: 귀여운 반려동물들을 홍보하는 게시판
  - 실종/목격 게시판: 반려동물 잃어버리거나 잃어버린 반려동물들을 봤을때 작성하는게시판

- **보호소찾기**:
  - 국가동물보호정보시스템 에 등록된 보호소를 조회할수있습니다.
  - 주소, 전화번호, 위치를 표시해 접근성을 높혔습니다.

- **입양신청서작성**:
  - 기본정보, 휴대폰, 거주지, 기본설문 및 입양사유를 적고 입양계약서를 작성해 보호소 이메일로 제출합니다.

- **마이페이지**:
  - 내정보수정 (닉네임, 이메일수정)
  - 입양신청 내역 조회
  - 찜 공고 확인
  - 내가 작성한 커뮤니티 게시글 확인
  - 좋아요한 게시글 확인

- **1:1채팅**:
  - 실종/목격 게시판에서 게시글 작성자와 1:1채팅 가능

# 3.1 차별점
| 구분 | 함께하개냥 | 기존 서비스 |
|---|---|---|
|실시간유기동물정보갱신|매일 2회 자동수집 및 반영 | 수동 또는 낮은빈도|
|소셜로그인| 카카오, 네이버, 구글 통합지원| 카카오지원|
|입양신청 자동화| 신청서 작성완료와 동시에 PDF변환+메일발송| 수동|
|1:1 실시간채팅|유저간 1:1 채팅기능|없음|

<br/>
<br/>

# 4. Tasks & Responsibilities (작업 및 역할 분담)
|  |  |  |
|-----------------|-----------------|-----------------|
| 선순주   | <img width="252" height="252" alt="Image" src="https://github.com/user-attachments/assets/98217954-b830-4f33-9a90-2ae967567ed9" /> | <ul><li>공통응답 API 구조설계 및 구현</li><li>실종/목격 제보게시판</li><li>S3, RDS 초기환경 설정</li><li>S3 이미지 관련 로직구현</li><li>사용자간 1:1채팅기능</li><li>전체적인 프론트엔드 연동</li><li>API 구조 및 DTO 전반수정</li></ul> |
| 박세정   | <img width="252" height="252" alt="Image" src="https://github.com/user-attachments/assets/27eebd22-1686-4649-a708-2e81c5835313" />  | <ul><li>마이페이지</li><li>입양후기게시판</li><li>좋아요,댓글</li><li>깃액션 연동</li><li>게시판 엘라스틱 서치 연동</li><li>docker compose file 작성</li></ul> |
| 엄아영   | <img width="252" height="252" alt="Image" src="https://github.com/user-attachments/assets/0a0b18ab-354d-4d73-89ab-b52b1a1778b5" />  | <ul><li>사용자정보수정</li><li>유기동물전체보기페이지</li><li>유기동물API연동</li><li>이달의 추천동물 통합검색</li><li>유기동물 통합검색</li><li>탄력적IP 설정</li></ul>  |
| 이지은   | <img width="252" height="252" alt="Image" src="https://github.com/user-attachments/assets/1eb4cd62-2c8c-4455-a779-0654f6fe0bed" />  | <ul><li>소셜로그인(구글) 연동 </li><li> 유기동물 상세보기페이지 작성 </li><li>입양신청서 작성, 입양신청서 메일전송</li><li>알림기능</li></ul>  |
| 오승훈   | <img width="252" height="252" alt="Image" src="https://github.com/user-attachments/assets/03e418c7-e72c-41ad-83bf-314f4832b57c" />  | <ul><li>JWT/SpringSecurity 초기설정</li><li>로그인/회원가입 공통로직 구현</li><li>소셜로그인 연동(카카오)</li><li>보호소 조회페이지 작성</li><li>보호소 상세조회 페이지 작성</li><li>카카오 지도 불러오기</li><li>깃액션 연동</li></ul>  |
| 이은서   | <img width="252" height="252" alt="Image" src="https://github.com/user-attachments/assets/024935dd-908b-4144-8083-e94eac8f1f83" /> | <ul><li>AWS 초기설정</li><li>SNS홍보게시판 작성</li><li>로그아웃 공통로직 구현, 회원탈퇴 구현</li><li>소셜로그인 연동 (네이버)</li><li>게시판 엘라스틱서치 연동</li><li>docker-compose file 작성</li><li>회원탈퇴 페이지 연동</li><li>깃허브 관리 (이슈템플릿작성 및 이슈관리, 리드미 작성)</li></ul>  |

<br/>

# 5. Technology Stack (기술 스택)
## 5.1 Language
|        |                 |    |
|--------|-----------------|---|
|Java  |<img width="50" height="30" alt="Image" src="https://github.com/user-attachments/assets/9f43f57e-9bca-4615-a1d4-79873d7a3f3c" />| Gradle 17|



<br/>

## 5.2 Frotend
|  |  |
|-----------------|-----------------|
| React    |  <img width="80"  alt="React" src="https://shields.io/badge/React-3080CA?logo=React&logoColor=FFF&style=flat-square"/> |
|TypeScript|<img width="100"  alt="TypeScript" src="https://shields.io/badge/TypeScript-3178C6?logo=TypeScript&logoColor=FFF&style=flat-square"/>|

<br/>

## 5.3 Backend
|  |  |
|-----------------|-----------------|
SpringBoot|<img width="100" src="https://img.shields.io/badge/SpringBoot-6DB33F?style=flat-square&logo=Spring&logoColor=white"/>
Mysql(8.0) <br>- local test|<img width="100" src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white"/>|




<br/>

## 5.4 Cooperation
|  |  |
|-----------------|-----------------|
| Github    |  <img width="100" alt="Github" src="https://shields.io/badge/github-000000?logo=github&logoColor=FFF&style=flat-square"/>     |
| Discord    |  <img width="100" alt="Discord" src="https://shields.io/badge/discord-3178C6?logo=discord&logoColor=FFF&style=flat-square"/>   |
| Notion    |  <img width="100"  src="https://shields.io/badge/notion-ffffff?logo=notion&logoColor=000000&style=flat-square"/>    |

## 5.5 API Test
| | |
|---|---|
|Swagger| <img width="100" src="https://img.shields.io/badge/-Swagger-85EA2D?style=flat&logo=swagger&logoColor=white"/> |
|Postman|  <img width="100" src="https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=Postman&logoColor=white"/>|

## 5.5 deployment
|  |  |
|---|---|
|EC2| <img width="100" height="30" src="https://img.shields.io/badge/Amazon_EC2-231F20?style=for-the-badge&logo=Amazon_EC2&logoColor=white"/>  |
|AmazonS3| <img width="100" height="30" src="https://img.shields.io/badge/Amazon_S3-231F20?style=for-the-badge&logo=Amazon_S3&logoColor=white"/>  |
|GitAction|<img width="100"  src="https://img.shields.io/badge/GithubActions-2088FF?style=for-the-badge&logo=GithubActions&logoColor=white"/>  |
|docker|<img width="100"  src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=Docker&logoColor=white"/>   |

## 5.6 infrastructure
|  |  |
|---|---|
|Kafka|  <img width="100" height="30" src="https://img.shields.io/badge/Apache_Kafka-231F20?style=for-the-badge&logo=apache-kafka&logoColor=white"/> |
|Apache Zookeeper| <img  width="100" height="30" src="https://img.shields.io/badge/Zookeeper-231F20?style=flat&logo=apache-zookeeper&logoColor=white"/>
|Redis| <img width="100" height="30" src="https://img.shields.io/badge/Redis-FF4438?style=flat&logo=Redis&logoColor=white"/>
|Websocket| <img width="100" height="30" src="https://img.shields.io/badge/Socket.io-010101?style=flat&logo=Socket.io&logoColor=white"/> |
|Elasticsearch|<img width="100" height="30" src="https://img.shields.io/badge/Elasticsearch-005571?style=flat&logo=Elasticsearch&logoColor=white"/>|
|Nginx| <img width="100" height="30" src="https://img.shields.io/badge/NGINX-009639?style=flat&logo=Elasticsearch&logoColor=white"/> |
|Stomp| <img width="100" height="30" src="https://img.shields.io/badge/STOMP-231F20?style=flat&logo=STOMP&logoColor=white"/> |




<br/>

# 6. Project Structure (프로젝트 구조)
```plaintext
nyang (루트)
├── .github                 # 깃허브 워크플로우 및 이슈 템플릿
│   ├─ ISSUE_TEMPLATE
│   ├─ scripts
│   └─ workflows
├── data                    # 로컬 DB설정
├── logs                    # 로그 디렉토리
├── monitoring              # 모니터링 툴
│   └─ nyang_http
│       ├─ logstash
│       └─ kibana-data
├── nginx                   # Nginx 설정
└── src
    ├─ main
    │   ├─ java/com/project/nyang
    │   │   ├─ global
    │   │   │   ├─ common (공통 API, 엔티티, 외부 Public API, S3)
    │   │   │   ├─ config (CORS, Security, Swagger 설정)
    │   │   │   ├─ elasticsearch (animal/board ES 모듈)
    │   │   │   ├─ exception (전역 예외 처리)
    │   │   │   ├─ searchlog (검색 로그, Kafka)
    │   │   │   └─ security (JWT, OAuth2)
    │   │   ├─ modules (도메인별 비즈니스 로직)
    │   │   │   ├─ adoption (입양 관리, 메일, PDF)
    │   │   │   ├─ animal (동물 정보)
    │   │   │   ├─ auth (회원 인증)
    │   │   │   ├─ board
    │   │   │   │   ├─ lost (실종/목격 게시판)
    │   │   │   │   ├─ review (입양 후기)
    │   │   │   │   └─ sns (SNS 홍보)
    │   │   │   ├─ chat (1:1 채팅, Redis)
    │   │   │   ├─ comment (댓글)
    │   │   │   ├─ image (이미지 관리, S3 업로드)
    │   │   │   ├─ like (좋아요)
    │   │   │   ├─ mypage (마이페이지)
    │   │   │   ├─ notification (알림)
    │   │   │   ├─ shelter (보호소 관리)
    │   │   │   └─ user (회원)
    │   │   └─ reference (참고용 코드)
    │   └─ resources
    │       ├─ fonts
    │       ├─ static
    │       └─ templates
    └─ test/java/com/project/nyang

```
# 6.1 ERD
<img width="771" height="761" alt="Image" src="https://github.com/user-attachments/assets/189ecf4f-9bf8-4432-9c91-4cecd736e372" />
<br/>
<br/>

# 7. Development Workflow (개발 워크플로우)
## 브랜치 전략 (Branch Strategy)
우리의 브랜치 전략은 Git Flow를 기반으로 하며, 다음과 같은 브랜치를 사용합니다.

- main          # 배포 브랜치 (항상 안정화 상태)
- dev           # 개발 통합 브랜치 (feature 병합)
- feature/*     # 기능 단위 브랜치 (feature/mypage)
  - 새로운 기능 개발 시 feature/{기능명} 브랜치 생성 후 작업
  - 완료된 기능은 develop 브랜치로 Pull Request 생성
  - main은 배포 시점에만 merge

## 개발절차
    1. feature 브랜치 생성
    2. 코드 작성 + 커밋
    3. PR 생성 (develop 기준)
    4. 코드 리뷰 → 승인 후 merge
    5. merge 후 feature 브랜치 삭제

# 8. Commit Convention (커밋컨밴션)
[커밋컨밴션 참고문서](https://www.notion.so/211e51228f4b81cc8f3deced0a87af6d)

# 9. Coding Convention (코딩컨벤션)
  - 패키지, 클래스, 메서드, 변수, 상수 모두 명명 규칙 준수
  - 반의어는 반드시 대응 개념 사용
  - DTO, Controller, Service, Repository 네이밍 통일

# 10. REST API 
  - URI는 명사형, 복수형으로 구성 (/users, /users/{id})
  - HTTP Method로 행위 구분 (GET, POST, PUT, DELETE)
  - 계층적 구조 반영 (/users/{id}/posts)

# 11. Code Review
  - 모든 Pull Request는 최소 1명 이상의 리뷰어 승인 후 merge
  - 코드 리뷰 시 기능 검증 + 컨벤션 준수 여부 확인
  - 리뷰 의견 반영 후 다시 요청
# 12. Deployment
    1. develop → main merge
    2. CI/CD 자동 배포 진행
    3. 배포 후 hotfix 필요 시 hotfix 브랜치 생성

# 13. 개발후기
[ 팀장_ 선순주 ] 
  - 갑작스레 팀장을 맡게 되어 많이 걱정되었지만, 함께했던 팀원들과 서로 의지하고 협력하는 과정에서 좋은 팀워크를 느낄 수 있었습니다. 부족한 저를 믿고 함께해준 팀원분들 너무 감사해요! 우리 팀원들 덕분에 짧은 기간동안 너무나도 값진 경험을 했습니다!

[ 팀원_박세정 ]
  - 명확한 기획과 체계화된 계획 아래 프로젝트가 수월하게 진행되어 큰 성취감을 느꼈습니다.

[ 팀원_엄아영 ]
  - 체계적으로 프로젝트를 완수한 것이 큰 자산이 된 것 같습니다. 그리고 팀원분들께 많은 것들을 배울 수 있어서 감사했습니다.

[ 팀원_이지은 ]
  - 이렇게 체계적으로 해본게 거의 처음인데 팀원들과 다 으쌰으쌰해서 기간 내에 마무리 된 것 같습니다! 모두에게 많은 것들을 배웠습니다. 감사합니다

[ 팀원_오승훈 ]
  - 3주라는 짧은 기간동안 서비스를 완성하기 힘들거라 생각했는데 다들 너무 열심히 해주셔서 많은 것을 배웠고 잘 마무리했습니다!

[ 팀원_이은서 ]
  - 시간이 생각보다 촉박하다 생각했는데, 팀원분들이 이끌어주시고, 같이 짐 들어주시니 일정맞추기 수월하였고, 서로 코드를 리뷰해주면서 놓친부분, 빠트린부분을 알려주셔서 훨씬 안정성이 높아진 프로젝트가 된것같습니다. 또한, 제가 이전에 배운 내용 (모니터링파트) 잘 활용하지못했는데 여러사람들이 도와주셔서 좋은결과를 가져간것같습니다. 감사합니다


