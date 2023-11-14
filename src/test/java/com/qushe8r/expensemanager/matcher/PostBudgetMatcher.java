package com.qushe8r.expensemanager.matcher;

import com.qushe8r.expensemanager.budget.dto.PostBudget;
import org.mockito.ArgumentMatcher;

public class PostBudgetMatcher implements ArgumentMatcher<PostBudget> {

  private final PostBudget left;

  public PostBudgetMatcher(PostBudget left) {
    this.left = left;
  }

  @Override
  public boolean matches(PostBudget right) {
    boolean isSameBudget = left.amount().equals(right.amount());
    boolean isSameMonth = left.month().equals(right.month());
    boolean isSameCategory = left.categoryId().equals(right.categoryId());
    return isSameBudget && isSameMonth && isSameCategory;
  }
}
