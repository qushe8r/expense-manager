package com.qushe8r.expensemanager.common.exception;

import lombok.Getter;

@Getter
public class GlobalException extends RuntimeException {

  private final ExceptionCode exceptionCode;

  public GlobalException(ExceptionCode exceptionCode) {
    super(exceptionCode.getMessage());
    this.exceptionCode = exceptionCode;
  }
}
