# Spring Boot & AWS 로 혼자 구현하는 웹 서비스
IntelliJ, JPA, Junit TEST, gradle, 소셜 로그인(OAuth2), AWS Infra 로 무중단 배포까지 실습합니다.

 이동욱 저
 

----
## Chapter
- [x] 1장 인텔리제이로 스프링부트 시작하기
- [x] 2장 스프링 부트에서 테스트 코드를 작성하자
- [x] 3장 스프링 부트에서 JPA 로 데이터베이스 다뤄보자
- [x] 4장 Mustache 로 화면 구성하기
- [x] 5장 스프링 시큐리티와 OAuth 2.0 으로 로그인 기능 구현하기
- [x] 6장 AWS 서버 환경을 만들어보자 - AWS EC2
- [x] 7장 AWS 에 데이터베이스 환경을 만들어 보자 - AWS RDS
- [x] 8장 EC2 서버에 프로젝트를 배포해 보자
- [x] 9장 Travis CI 배포 자동화
- [ ] 10장 무중단 배포
    * 무중단 배포: 서비스를 중지 하지 않고 배포를 하는 것


----
## tools
- Spring Boot 2.1.4.RELEASE
- Mustache
- JPA
- MariaDB
- h2 (dev)
- Cloud
  * AWS
    * EC2
    * RDS
    * S3
    * CodeDeploy
    * IAM (S3, CodeDeploy)
  * Travis CI
  * Nginx


### Travis CI 배포 자동화
GitHub -> Travis CI (빌드 결과물 : jar 파일) -> AWS S3 (jar 파일 저장)
-> Travis CI (배포 요청) -> AWS CodeDeploy (S3 에 저장된 jar 파일 배포) -> AWS EC2 -> SpringBoot(인스턴스 내부) <- 사용자 