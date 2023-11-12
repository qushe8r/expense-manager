package com.qushe8r.expensemanager.security.exception;

import com.qushe8r.expensemanager.common.exception.ExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthExceptionCode implements ExceptionCode {
  INFORMATION_NOT_MATCHED("AX01", 400, "잘못된 정보를 입력하셨습니다.");

  private final String errorCode;

  private final Integer status;

  private final String message;
}
