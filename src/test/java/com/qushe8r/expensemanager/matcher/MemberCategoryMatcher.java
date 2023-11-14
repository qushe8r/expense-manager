package com.qushe8r.expensemanager.matcher;

import com.qushe8r.expensemanager.category.entity.MemberCategory;
import org.mockito.ArgumentMatcher;

public class MemberCategoryMatcher implements ArgumentMatcher<MemberCategory> {

  private final MemberCategory left;

  public MemberCategoryMatcher(MemberCategory left) {
    this.left = left;
  }

  @Override
  public boolean matches(MemberCategory right) {
    boolean isSameId = isSameId(left.getId(), right.getId());
    boolean isSameCategoryId = isSameId(left.getCategory().getId(), right.getCategory().getId());
    boolean isSameMemberId = isSameId(left.getMember().getId(), right.getMember().getId());
    return isSameId && isSameCategoryId && isSameMemberId;
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
