package com.qushe8r.expensemanager.common.exception;

public class ValidateEndBeforeStartException extends GlobalException {

  public ValidateEndBeforeStartException() {
    super(ValidateExceptionCode.END_AFTER_START);
  }
}
