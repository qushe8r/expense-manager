package com.qushe8r.expensemanager.matcher;

import com.qushe8r.expensemanager.category.entity.Category;
import org.mockito.ArgumentMatcher;

public class CategoryMatcher implements ArgumentMatcher<Category> {

  private final Category left;

  public CategoryMatcher(Category left) {
    this.left = left;
  }

  @Override
  public boolean matches(Category right) {
    boolean isSameId = isSameId(left, right);
    boolean isSameName = left.getName().equals(right.getName());
    return isSameId && isSameName;
  }

  private boolean isSameId(Category left, Category right) {
    if (left.getId() == null && right.getId() == null) {
      return true;
    }
    if (left.getId() != null && right.getId() != null) {
      return left.getId().equals(right.getId());
    }
    return false;
  }
}
