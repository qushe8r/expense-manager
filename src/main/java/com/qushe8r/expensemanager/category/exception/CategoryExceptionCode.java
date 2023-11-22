package com.qushe8r.expensemanager.category.exception;

import com.qushe8r.expensemanager.common.exception.ExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CategoryExceptionCode implements ExceptionCode {
  CATEGORY_NOT_FOUND("MX01", 404, "존재하지 않는 카테고리입니다."),
  CATEGORY_ALREADY_EXIST("MX02", 409, "이미 존재하는 카테고리입니다.");

  private final String errorCode;

  private final Integer status;

  private final String message;
}
