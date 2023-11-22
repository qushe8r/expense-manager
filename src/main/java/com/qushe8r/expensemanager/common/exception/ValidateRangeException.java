package com.qushe8r.expensemanager.common.exception;

public class ValidateRangeException extends GlobalException {

  public ValidateRangeException() {
    super(ValidateExceptionCode.INVALID_AMOUNT_RANGE);
  }
}
