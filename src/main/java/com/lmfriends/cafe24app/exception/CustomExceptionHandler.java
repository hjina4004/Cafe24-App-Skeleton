package com.lmfriends.cafe24app.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.log4j.Log4j2;

@Log4j2
@ControllerAdvice
public class CustomExceptionHandler {

  @ExceptionHandler(CustomException.class)
  protected ResponseEntity<String> handleCustomException(CustomException e) {
    log.error("CustomExceptionHandler {}", e.getMessage());
    return ResponseEntity
        .status(e.getErrorCode())
        .body(e.getMessage());
  }
}
