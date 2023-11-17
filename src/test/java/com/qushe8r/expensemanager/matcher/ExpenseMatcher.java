package com.qushe8r.expensemanager.matcher;

import com.qushe8r.expensemanager.category.entity.Category;
import com.qushe8r.expensemanager.category.entity.MemberCategory;
import com.qushe8r.expensemanager.expense.entity.Expense;
import com.qushe8r.expensemanager.member.entity.Member;
import org.mockito.ArgumentMatcher;

public class ExpenseMatcher implements ArgumentMatcher<Expense> {

  private final Expense left;

  public ExpenseMatcher(Expense left) {
    this.left = left;
  }

  @Override
  public boolean matches(Expense right) {
    boolean isSameId = isSameId(left.getId(), right.getId());
    boolean isSameAmount = left.getAmount().equals(right.getAmount());
    boolean isSameMemo = left.getMemo().equals(right.getMemo());
    boolean isSameMemberCategory =
        isSameMemberCategory(left.getMemberCategory(), right.getMemberCategory());
    return isSameId && isSameAmount && isSameMemo && isSameMemberCategory;
  }

  private boolean isSameMemberCategory(MemberCategory left, MemberCategory right) {
    if (left == null && right == null) {
      return true;
    }
    if (left != null && right != null) {
      boolean isSameId = isSameId(left.getId(), right.getId());
      boolean isSameMember = isSameMember(left.getMember(), right.getMember());
      boolean isSameCategory = isSameCategory(left.getCategory(), right.getCategory());
      return isSameId && isSameMember && isSameCategory;
    }
    return false;
  }

  private boolean isSameCategory(Category left, Category right) {
    if (left == null && right == null) {
      return true;
    }
    if (left != null && right != null) {
      return isSameId(left.getId(), right.getId());
    }
    return false;
  }

  private boolean isSameMember(Member left, Member right) {
    if (left == null && right == null) {
      return true;
    }
    if (left != null && right != null) {
      return isSameId(left.getId(), right.getId());
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
