package com.qushe8r.expensemanager.expense.exception;

import com.qushe8r.expensemanager.common.exception.GlobalException;

public class ExpenseNotFoundException extends GlobalException {

  public ExpenseNotFoundException() {
    super(ExpenseExceptionCode.EXPENSE_NOT_FOUND);
  }
}
