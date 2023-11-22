package com.qushe8r.expensemanager.matcher;

import com.qushe8r.expensemanager.expense.dto.PatchExpense;
import org.mockito.ArgumentMatcher;

public class PatchExpenseMatcher implements ArgumentMatcher<PatchExpense> {

  private final PatchExpense left;

  public PatchExpenseMatcher(PatchExpense left) {
    this.left = left;
  }

  @Override
  public boolean matches(PatchExpense right) {
    boolean isSameAmount = left.amount().equals(right.amount());
    boolean isSameExpenseAt = left.expenseAt().equals(right.expenseAt());
    boolean isSameMemo = isSameMemo(left.memo(), right.memo());
    boolean isSameCategoryId = left.categoryId().equals(right.categoryId());
    return isSameAmount && isSameExpenseAt && isSameMemo && isSameCategoryId;
  }

  private boolean isSameMemo(String left, String right) {
    if (left == null && right == null) {
      return true;
    }
    if (left != null && right != null) {
      return left.equals(right);
    }
    return false;
  }
}
