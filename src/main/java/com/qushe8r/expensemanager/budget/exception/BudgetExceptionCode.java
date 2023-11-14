package com.qushe8r.expensemanager.budget.exception;

import com.qushe8r.expensemanager.common.exception.ExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BudgetExceptionCode implements ExceptionCode {
  BUDGET_NOT_FOUND("BX01", 404, "예산을 찾을 수 없습니다."),
  BUDGET_ALREADY_EXIST("BX02", 409, "이미 예산이 존재합니다.");

  private final String errorCode;

  private final Integer status;

  private final String message;

}
