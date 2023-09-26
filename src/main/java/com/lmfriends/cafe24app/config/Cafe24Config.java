package com.lmfriends.cafe24app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
public class Cafe24Config {

  @Value("${cafe24.APP_NAME}")
  private String appName;

  @Value("${cafe24.APP_URI}")
  private String appUri;

  @Value("${cafe24.CLIENT_ID}")
  private String clientId;

  @Value("${cafe24.CLIENT_SECRET}")
  private String clientSecret;

  @Value("${cafe24.APP_SCOPE}")
  private String appScope;
}
