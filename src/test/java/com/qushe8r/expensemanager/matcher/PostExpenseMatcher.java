package com.qushe8r.expensemanager.matcher;

import com.qushe8r.expensemanager.expense.dto.PostExpense;
import org.mockito.ArgumentMatcher;

public class PostExpenseMatcher implements ArgumentMatcher<PostExpense> {

  private final PostExpense left;

  public PostExpenseMatcher(PostExpense left) {
    this.left = left;
  }

  @Override
  public boolean matches(PostExpense right) {
    boolean isSameAmount = left.amount().equals(right.amount());
    boolean isSameMemo = left.memo().equals(right.memo());
    boolean isSameExpenseAt = left.expenseAt().equals(right.expenseAt());
    boolean isSameCategory = left.categoryId().equals(right.categoryId());
    return isSameAmount && isSameMemo && isSameExpenseAt && isSameCategory;
  }
}
