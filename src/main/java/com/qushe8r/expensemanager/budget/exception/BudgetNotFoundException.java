package com.qushe8r.expensemanager.budget.exception;

import com.qushe8r.expensemanager.common.exception.GlobalException;

public class BudgetNotFoundException extends GlobalException {

  public BudgetNotFoundException() {
    super(BudgetExceptionCode.BUDGET_NOT_FOUND);
  }
}
