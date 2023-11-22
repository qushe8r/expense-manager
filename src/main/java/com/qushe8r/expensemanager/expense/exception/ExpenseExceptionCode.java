package com.qushe8r.expensemanager.expense.exception;

import com.qushe8r.expensemanager.common.exception.ExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExpenseExceptionCode implements ExceptionCode {
  EXPENSE_NOT_FOUND("EX01", 404, "예산을 찾을 수 없습니다.");

  private final String errorCode;

  private final Integer status;

  private final String message;
}
