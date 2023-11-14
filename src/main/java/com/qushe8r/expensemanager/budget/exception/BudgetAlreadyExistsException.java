package com.qushe8r.expensemanager.budget.exception;

import com.qushe8r.expensemanager.common.exception.GlobalException;

public class BudgetAlreadyExistsException extends GlobalException {

  public BudgetAlreadyExistsException() {
    super(BudgetExceptionCode.BUDGET_ALREADY_EXIST);
  }
}
