package com.lmfriends.cafe24app.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class CustomException extends RuntimeException {

  private Integer errorCode;
  private String message;
}
