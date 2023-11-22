package com.qushe8r.expensemanager.member.exception;

import com.qushe8r.expensemanager.common.exception.GlobalException;

public class MemberAlreadyExistException extends GlobalException {

  public MemberAlreadyExistException() {
    super(MemberExceptionCode.MEMBER_ALREADY_EXIST);
  }
}
