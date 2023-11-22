package com.qushe8r.expensemanager.category.entity;

import com.qushe8r.expensemanager.member.entity.Member;
import java.util.Collections;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class MemberCategoryTest {

  @Test
  void constructor() {
    // given
    Long memberCategoryId = 1L;

    Member member = new Member(1L);
    Category category = new Category(1L);

    // when
    MemberCategory memberCategory = new MemberCategory(memberCategoryId, member, category);

    // then
    Assertions.assertThat(memberCategory)
        .hasFieldOrPropertyWithValue("id", memberCategoryId)
        .hasFieldOrProperty("member")
        .hasFieldOrProperty("category");
  }

  @Test
  void constructorNonId() {
    // given
    Member member = new Member(1L);
    Category category = new Category(1L);

    // when
    MemberCategory memberCategory = new MemberCategory(null, member, category);

    // then
    Assertions.assertThat(memberCategory)
        .hasFieldOrPropertyWithValue("id", null)
        .hasFieldOrProperty("member")
        .hasFieldOrProperty("category");
  }

  @Test
  void getter() {
    // given
    Long memberCategoryId = 1L;

    Member member = new Member(1L);
    Category category = new Category(1L);

    // when
    MemberCategory memberCategory = new MemberCategory(memberCategoryId, member, category);

    // then
    Assertions.assertThat(memberCategory.getId()).isEqualTo(memberCategoryId);
    Assertions.assertThat(memberCategory.getMember()).isNotNull();
    Assertions.assertThat(memberCategory.getCategory()).isNotNull();
    Assertions.assertThat(memberCategory.getBudgets()).isEqualTo(Collections.emptyList());
  }
}
