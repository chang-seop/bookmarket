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

### 🚧 요구사항 및 진행
#### 멤버 기능 (유저)
- [x] 회원가입
- [x] 로그인
- [x] 마이페이지
- [x] 프로필 & 멤버 수정
- [x] 닉네임, 주소 이름 수정
- [x] 프로필 상메 수정
- [x] 프로필 이미지 수정
- [ ] 프로필 이미지는 최대 1개 검증

#### 멤버 기능 (판매자)
- [x] 판매자 등록

#### 멤버 기능 (어드민)
- [x] 판매자 권한 등록 조회(페이징)
- [x] 판매자 권한 등록 세부 조회 
- [x] 권한 부여, 취소

#### 책 상품 기능 (판매자)
- [x] 상품, 이미지 등록
- [ ] 등록, 수정시 메인 이미지는 최대 1개 검증, 서브 이미지는 최대 5개 검증
- [X] 등록 상품 조회(페이징)
- [ ] 검색 조건 기능
- [X] 상품 수정 
- [ ] 상품 삭제 (모든 주문이 완료되어 있는 상태여야 한다)
- [ ] 삭제시 연관된 상품 이미지도 일괄 삭제 처리
- [X] 판매자 정보 수정
- [ ] 상품 등록 시 카테고리 설정

#### 책 상품 기능 (유저)
- [X] 상품 조회(페이징)
- [ ] 검색 조건 기능
- [X] 단일 상품 조회(detail)

#### 카테고리 기능 (어드민)
- [ ] 카테고리 등록
- [ ] 카테고리 수정
- [ ] 카테고리 삭제

#### 주문 기능 (유저)
- [ ] 주문 등록
- [ ] 주문 취소
- [ ] 주문 조회
- [ ] 주문 배달 조회

#### 배달 기능 (유저)
- [ ] 배달 등록
- [ ] 배달 취소

#### 장바구니 기능 (유저)
- [ ] 장바구니 등록(책)
- [ ] 장바구니 삭제
- [ ] 장바구니 수정
- [ ] 장바구니 조회

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
  - com.mysql:mysql-connector-j
  - lombok

<br/>
<hr/>

### 배포 과정
#### 사용 툴
- VMWare Workstation 17 player, Putty, FileZilla
- HostOS: Windows 11
- guestOS: CentOS Stream 9

#### 버전
- nginx: 1.20.1
- MySQL: 8.4.1
- JDK: 17.0.6

#### 순서
1. 가상 OS VM Ware 프로그램 설치
2. 운영체제 CentOS Stream 9 설치
3. Putty로 접속할 관리자 계정 생성
4. 접속은 외부 서버가 있다고 가정하여 Putty로 SSH 접속 및 파일은 FileZilla를 통해 파일 옮기기
5. Java Development Kit(JDK) 설치
6. nginx 설치, 접속 테스트
7. Host OS 공유기에 포트 포워딩 설정(80, 443), VMWare 포트 포워딩 설정(80, 443 외부 접속 허용)
8. Firewall(방화벽) 80, 443 포트 열기
9. '내도메인.한국' 무료 DNS 사용 및 Host 외부 IP 연결
10. MySQL 설치 및 계정 생성, 데이터베이스 생성 및 테이블 추가
11. Spring Boot 이용하여 빌드(BootJar) 후 파일 systemctl(서비스) 등록
12. Certbot 이용하여 무료 lets' encrypt SSL/TLS 설치 및 nginx.conf 파일 설정
13. 서비스 실행 및 외부 사용자 접속 테스트

#### nginx.conf
```nginx
user nginx;
worker_processes auto;
error_log /var/log/nginx/error.log;
pid /run/nginx.pid;

events {
    worker_connections 1024;
}

http {
    client_max_body_size 10M;

    upstream app {
       server 127.0.0.1:8080;
    }

    underscores_in_headers on;
    include /etc/nginx/mime.types;
    default_type application/octet-stream;

    # Redirect all Traffic to HTTPS
    server {
      listen 80;

	  location ~ /\.well-known/acme-challenge/ {
	  	allow all;
		root /var/www/letsencrypt;
	  }

      return 301 https://$host$request_uri;
    }
 
    server {
      listen 443 ssl http2;
      ssl_certificate /etc/letsencrypt/live/nineto6.p-e.kr/fullchain.pem;
      ssl_certificate_key /etc/letsencrypt/live/nineto6.p-e.kr/privkey.pem;

      # Disable SSL
      ssl_protocols TLSv1 TLSv1.1 TLSv1.2;
     
      # 통신과정에서 사용할 암호화 알고리즘
      ssl_prefer_server_ciphers on;
      ssl_ciphers ECDH+AESGCM:ECDH+AES256:ECDH+AES128:DH+3DES:!ADH:!AECDH:!MD5;

      # Enable HSTS
      # client의 browser에게 http로 어떠한 것도 load하지 말라고 규제합니다.
      add_header Strict-Transport-Security "max-age=31536000" always;

      # SSL sessions
      ssl_session_cache shared:SSL:10m;
      ssl_session_timeout 10m;
    
      # Back-End Websocket
      location ^~/api/ws/ {
        proxy_pass http://app;
 
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection "Upgrade";
        proxy_set_header Host $host;
      }

      # Back-End API
      location ^~/api/ {
        proxy_pass http://app;
        proxy_set_header Host $host;
        proxy_set_header X-Nginx-Proxy true;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
      }

      # Front-End React
      location / {
        root /home/ikavon/react/build;
        index index.html;
      	try_files $uri $uri/ /index.html;
      } 
    }
}
```

#### 96talk.service
```
[Unit]
Description=BookStore Description
After=syslog.target network.target bookstore.service

[Service]
ExecStart=/bin/bash -c "exec java -jar /home/ikavon/server/bookstore/book-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod"
Restart=on-failure
RestartSec=10

User=root
group=root

[Install]
WantedBy=multi-user.taget
```