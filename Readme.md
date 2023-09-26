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
요청헤더에 X-Api-Authority 설정 필요

* refresh_token를 사용하여 Access Token을 재발급
  ```
  /api/refresh-token/{mallId}/{shopNo}
  ```

* 쇼핑몰 회원 조회
  ```
  /api/customers/{mallId}/{shopNo}/{memberId}
  ```

* 쇼핑몰 주문 목록
  ```
  /api/orders/{mallId}/{shopNo}?start_date=2021-06-01&end_date=2021-06-30&embed=items,receivers,buyer,return,cancellation,exchange
  start_date default: 요청일 해당월 첫째일
  end_date default: 요청일
  ```

* 쇼핑몰 주문 조회
  ```
  /api/orders/{mallId}/{shopNo}/{orderId}?embed=items,receivers,buyer,return,cancellation,exchange
  ```
