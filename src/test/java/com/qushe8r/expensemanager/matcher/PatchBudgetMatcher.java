package com.qushe8r.expensemanager.matcher;

import com.qushe8r.expensemanager.budget.dto.PatchBudget;
import org.mockito.ArgumentMatcher;

public class PatchBudgetMatcher implements ArgumentMatcher<PatchBudget> {

  private final PatchBudget left;

  public PatchBudgetMatcher(PatchBudget left) {
    this.left = left;
  }

  @Override
  public boolean matches(PatchBudget right) {
    return left.amount().equals(right.amount());
  }
}
