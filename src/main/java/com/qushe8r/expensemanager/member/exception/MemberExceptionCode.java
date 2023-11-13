package com.qushe8r.expensemanager.member.exception;

import com.qushe8r.expensemanager.common.exception.ExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberExceptionCode implements ExceptionCode {
  MEMBER_NOT_FOUND("MX01", 404, "존재하지 않는 회원입니다."),
  MEMBER_ALREADY_EXIST("MX02", 409, "이미 존재하는 회원입니다.");

  private final String errorCode;

  private final Integer status;

  private final String message;
}
