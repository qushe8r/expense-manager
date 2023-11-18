package com.qushe8r.expensemanager.budget.entity;

import com.qushe8r.expensemanager.category.entity.Category;
import com.qushe8r.expensemanager.category.entity.MemberCategory;
import com.qushe8r.expensemanager.member.entity.Member;
import java.time.YearMonth;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class BudgetTest {

  @Test
  void constructor() {
    // given
    Long budgetId = 1L;
    Long amount = 10000L;
    YearMonth month = YearMonth.now();

    Member member = new Member(1L);
    Category category = new Category(1L);
    MemberCategory memberCategory = new MemberCategory(member, category);

    // when
    Budget budget = new Budget(budgetId, amount, month, memberCategory);

    // then
    Assertions.assertThat(budget)
        .hasFieldOrPropertyWithValue("id", budgetId)
        .hasFieldOrPropertyWithValue("amount", amount)
        .hasFieldOrPropertyWithValue("month", month);
  }

  @Test
  void constructorNonId() {
    // given
    Long budgetId = 1L;
    Long amount = 10000L;
    YearMonth month = YearMonth.now();

    Member member = new Member(1L);
    Category category = new Category(1L);
    MemberCategory memberCategory = new MemberCategory(member, category);

    // when
    Budget budget = new Budget(amount, month, memberCategory);

    // then
    Assertions.assertThat(budget)
        .hasFieldOrPropertyWithValue("id", null)
        .hasFieldOrPropertyWithValue("amount", amount)
        .hasFieldOrPropertyWithValue("month", month);
  }

  @Test
  void getter() {
    // given
    Long budgetId = 1L;
    Long amount = 10000L;
    YearMonth month = YearMonth.now();

    Member member = new Member(1L);
    Category category = new Category(1L);
    MemberCategory memberCategory = new MemberCategory(member, category);

    // when
    Budget budget = new Budget(budgetId, amount, month, memberCategory);

    // then
    Assertions.assertThat(budget.getId()).isEqualTo(budgetId);
    Assertions.assertThat(budget.getAmount()).isEqualTo(amount);
    Assertions.assertThat(budget.getMonth()).isEqualTo(month);
    Assertions.assertThat(budget.getMemberCategory())
        .isNotNull()
        .hasFieldOrPropertyWithValue("id", null);
  }
}
