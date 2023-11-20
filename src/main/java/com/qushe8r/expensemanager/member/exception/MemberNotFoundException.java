package com.qushe8r.expensemanager.member.exception;

import com.qushe8r.expensemanager.common.exception.GlobalException;

public class MemberNotFoundException extends GlobalException {

  public MemberNotFoundException() {
    super(MemberExceptionCode.MEMBER_NOT_FOUND);
  }
}
