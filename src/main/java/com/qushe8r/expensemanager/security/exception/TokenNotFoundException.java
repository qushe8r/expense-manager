package com.qushe8r.expensemanager.security.exception;

import com.qushe8r.expensemanager.common.exception.GlobalException;

public class TokenNotFoundException extends GlobalException {

  public TokenNotFoundException() {
    super(JwtExceptionCode.TOKEN_NOT_FOUND);
  }
}
