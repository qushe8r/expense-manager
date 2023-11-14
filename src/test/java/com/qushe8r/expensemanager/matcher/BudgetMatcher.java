package com.qushe8r.expensemanager.matcher;

import com.qushe8r.expensemanager.budget.entity.Budget;
import com.qushe8r.expensemanager.category.entity.Category;
import com.qushe8r.expensemanager.category.entity.MemberCategory;
import com.qushe8r.expensemanager.member.entity.Member;
import org.mockito.ArgumentMatcher;

public class BudgetMatcher implements ArgumentMatcher<Budget> {

  private final Budget left;

  public BudgetMatcher(Budget left) {
    this.left = left;
  }

  @Override
  public boolean matches(Budget right) {
    boolean isSameId = isSameId(left.getId(), right.getId());
    boolean isSameAmount = left.getAmount().equals(right.getAmount());
    boolean isSameMonth = left.getMonth().equals(right.getMonth());
    boolean isSameMemberCategory =
        isSameMemberCategory(left.getMemberCategory(), right.getMemberCategory());
    return isSameId && isSameAmount && isSameMonth && isSameMemberCategory;
  }

  private boolean isSameMemberCategory(MemberCategory left, MemberCategory right) {
    if (left == null && right == null) {
      return true;
    }
    boolean isSameId = isSameId(left.getId(), right.getId());
    boolean isSameMember = isSameMember(left.getMember(), right.getMember());
    boolean isSameCategory = isSameCategory(left.getCategory(), right.getCategory());
    return isSameId && isSameMember && isSameCategory;
  }

  private boolean isSameCategory(Category left, Category right) {
    return isSameId(left.getId(), right.getId());
  }

  private boolean isSameMember(Member left, Member right) {
    if (left.getId() == null && right.getId() == null) {
      return true;
    }
    if (left.getId() != null && right.getId() != null) {
      return left.getId().equals(right.getId());
    }
    return false;
  }

  private boolean isSameId(Long left, Long right) {
    if (left == null && right == null) {
      return true;
    }
    if (left != null && right != null) {
      return left.equals(right);
    }
    return false;
  }
}
