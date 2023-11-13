package com.qushe8r.expensemanager.macher;

import com.qushe8r.expensemanager.category.dto.PostCategory;
import org.mockito.ArgumentMatcher;

public class PostCategoryMatcher implements ArgumentMatcher<PostCategory> {

  private final PostCategory left;

  public PostCategoryMatcher(PostCategory left) {
    this.left = left;
  }

  @Override
  public boolean matches(PostCategory right) {
    return left.name().equals(right.name());
  }
}
