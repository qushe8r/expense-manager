package com.qushe8r.expensemanager.category.entity;

import java.util.Collections;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class CategoryTest {

  @Test
  void constructor() {
    // given
    Long categoryId = 1L;
    String categoryName = "카테고리";

    // when
    Category category = new Category(categoryId, categoryName);

    // then
    Assertions.assertThat(category)
        .hasFieldOrPropertyWithValue("id", categoryId)
        .hasFieldOrPropertyWithValue("name", categoryName)
        .hasFieldOrPropertyWithValue("memberCategories", Collections.emptyList());
  }

  @Test
  void constructorNonId() {
    // given
    String categoryName = "카테고리";

    // when
    Category category = new Category(null, categoryName);

    // then
    Assertions.assertThat(category)
        .hasFieldOrPropertyWithValue("id", null)
        .hasFieldOrPropertyWithValue("name", categoryName)
        .hasFieldOrPropertyWithValue("memberCategories", Collections.emptyList());
  }

  @Test
  void constructorOnlyId() {
    // given
    Long categoryId = 1L;

    // when
    Category category = new Category(categoryId, null);

    // then
    Assertions.assertThat(category)
        .hasFieldOrPropertyWithValue("id", categoryId)
        .hasFieldOrPropertyWithValue("name", null)
        .hasFieldOrPropertyWithValue("memberCategories", Collections.emptyList());
  }

  @Test
  void getter() {
    // given
    Long categoryId = 1L;
    String categoryName = "카테고리";

    // when
    Category category = new Category(categoryId, categoryName);

    // then
    Assertions.assertThat(category.getId()).isEqualTo(categoryId);
    Assertions.assertThat(category.getName()).isEqualTo(categoryName);
    Assertions.assertThat(category.getMemberCategories()).isEmpty();
  }
}
