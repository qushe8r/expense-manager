package com.qushe8r.expensemanager.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ValidateExceptionCode implements ExceptionCode {
  END_AFTER_START("PX01", 400, "종료일이 시작일보다 빠릅니다."),
  INVALID_DURATION("PX02", 400, "최대 조회 일수는 한달입니다."),
  INVALID_AMOUNT_RANGE("PX03", 400, "금액의 범위가 잘못 지정 되었습니다.");

  private final String errorCode;

  private final Integer status;

  private final String message;
}
