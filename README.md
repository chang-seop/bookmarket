## 개인 - 북 스토어 토이 프로젝트

<hr/>

### ⚡프로젝트 소개

#### 시작하게 된 이유
> - JPA, JPQL, Spring Data JPA, QueryDSL 익숙해질 수 있게 연습
> - 테이블 설계 연습
> - 권한에 따른 사용자, 판매자, 관리자 연습 (Spring Security)
> - 질의 N + 1 문제 해결 연습 (join fetch(다대일), default_batch_fetch_size(일대다 페이징 처리))

<hr/>

### ⚙️ 기술 정보
<div>
<img src="https://img.shields.io/badge/Java-007396?style=flat-square&logo=java&logoColor=FFFFFF"/>
<img src="https://img.shields.io/badge/Spring-6DB33F?style=flat-square&logo=spring&logoColor=FFFFFF"/>
<img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=flat-square&logo=springboot&logoColor=FFFFFF"/>
<img src="https://img.shields.io/badge/Spring Security-6DB33F?style=flat-square&logo=springsecurity&logoColor=FFFFFF"/>
<img src="https://img.shields.io/badge/MYSQL-4479A1?style=flat-square&logo=mysql&logoColor=FFFFFF"/>
<img src="https://img.shields.io/badge/H2 Database-4479A1.svg?&style=flat-square&logo=&logoColor=white">
<img src="https://img.shields.io/badge/Thymeleaf-005F0F.svg?&style=flat-square&logo=thymeleaf&logoColor=white">
<img src="https://img.shields.io/badge/JPA-59666C.svg?&style=flat-square&logo=Hibernate&logoColor=white">
<img src="https://img.shields.io/badge/Spring%20Data%20JPA-6DB33F.svg?&style=flat-square&logo=Spring&logoColor=white">
<img src="https://img.shields.io/badge/Querydsl-007ACC.svg?&style=flat-square&logoColor=white">

<hr/>

### 🚧 버전 관리 및 진행

<hr/>

### 🔍 프로젝트 상세

#### ERD-Cloud
![image-erd](./md_resource/image-erd.png)

<hr/>

### 🌳 개발 환경

- Project: Gradle
- SpringBoot: 3.2.4
- Language: Java 17
- Dependencies
  - spring-boot-starter-data-jpa
  - querydsl-jpa:5.0.0:jakarta
  - p6spy-spring-boot-starter:1.9.0
  - thymeleaf-extras-springsecurity6
  - spring-boot-starter-security
  - spring-boot-starter-web
  - spring-boot-starter-validation
  - spring-boot-starter-thymeleaf
  - spring-boot-starter-test
  - lombok