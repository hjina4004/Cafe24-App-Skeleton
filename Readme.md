# Cafe24 App Skeleton V1.0
개발환경
* Spring Boot 3.1.4
* Java 17
* Thymeleaf


## Cafe24 App 환경설정
config/application.yml
  ```
  cafe24:
    APP_NAME: cafe24App
    APP_URI: https://lmfriends.app/xxxx/app
    CLIENT_ID: xxxxxx
    CLIENT_SECRET: xxxxxx
    SERVICE_KEY: xxxxxx
    APP_SCOPE: mall.write_application, mall.read_application, mall.write_order, mall.read_order, ...
  ```


## Cafe24 REST api
* refresh_token를 사용하여 Access Token을 재발급
  ```
  refresh-token/{mallId}/{shopNo}
  ```

* 쇼핑몰 회원 검색
  ```
  customers/{mallId}/{shopNo}/{memberId}
  ```
