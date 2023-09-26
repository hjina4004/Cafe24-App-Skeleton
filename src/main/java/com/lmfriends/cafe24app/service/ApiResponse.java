package com.lmfriends.cafe24app.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ApiResponse<T> {

  private int code;
  private String message;
  private T data;

  public ApiResponse(final int code, final String message) {
    this.code = code;
    this.message = message;
    this.data = null;
  }

  public static <T> ApiResponse<T> res(final int code, final String message) {
    return res(code, message, null);
  }

  public static <T> ApiResponse<T> res(final int code, final String message, final T t) {
    return ApiResponse.<T>builder()
        .data(t)
        .code(code)
        .message(message)
        .build();
  }
}