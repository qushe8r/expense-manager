package com.qushe8r.expensemanager.common.exception;

public class ValidateDurationException extends GlobalException {

  public ValidateDurationException() {
    super(ValidateExceptionCode.INVALID_DURATION);
  }
}
