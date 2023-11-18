package com.qushe8r.expensemanager.expense.entity;

import com.qushe8r.expensemanager.category.entity.Category;
import com.qushe8r.expensemanager.category.entity.MemberCategory;
import com.qushe8r.expensemanager.expense.dto.PatchExpense;
import com.qushe8r.expensemanager.member.entity.Member;
import java.time.LocalDateTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ExpenseTest {

  @Test
  void constructor() {
    // given
    Long expenseId = 1L;
    Long amount = 10000L;
    String memo = "김치찌개";
    LocalDateTime expenseAt = LocalDateTime.now();

    Member member = new Member(1L);
    Category category = new Category(1L);
    MemberCategory memberCategory = new MemberCategory(member, category);

    // when
    Expense expense = new Expense(expenseId, amount, memo, expenseAt, memberCategory);

    // then
    Assertions.assertThat(expense)
        .hasFieldOrPropertyWithValue("id", expenseId)
        .hasFieldOrPropertyWithValue("amount", amount)
        .hasFieldOrPropertyWithValue("memo", memo)
        .hasFieldOrPropertyWithValue("expenseAt", expenseAt)
        .hasFieldOrPropertyWithValue("memberCategory", memberCategory);
  }

  @Test
  void constructorNonId() {
    // given
    Long amount = 10000L;
    String memo = "김치찌개";
    LocalDateTime expenseAt = LocalDateTime.now();

    Member member = new Member(1L);
    Category category = new Category(1L);
    MemberCategory memberCategory = new MemberCategory(member, category);

    // when
    Expense expense = new Expense(amount, memo, expenseAt, memberCategory);

    // then
    Assertions.assertThat(expense)
        .hasFieldOrPropertyWithValue("id", null)
        .hasFieldOrPropertyWithValue("amount", amount)
        .hasFieldOrPropertyWithValue("memo", memo)
        .hasFieldOrPropertyWithValue("expenseAt", expenseAt)
        .hasFieldOrPropertyWithValue("memberCategory", memberCategory);
  }

  @Test
  void getter() {
    // given
    Long expenseId = 1L;
    Long amount = 10000L;
    String memo = "김치찌개";
    LocalDateTime expenseAt = LocalDateTime.now();

    Member member = new Member(1L);
    Category category = new Category(1L);
    MemberCategory memberCategory = new MemberCategory(member, category);

    // when
    Expense expense = new Expense(expenseId, amount, memo, expenseAt, memberCategory);

    // then
    Assertions.assertThat(expense.getId()).isEqualTo(expenseId);
    Assertions.assertThat(expense.getAmount()).isEqualTo(amount);
    Assertions.assertThat(expense.getExpenseAt()).isEqualTo(expenseAt);
    Assertions.assertThat(expense.getMemberCategory())
        .isNotNull()
        .hasFieldOrPropertyWithValue("id", null);
  }

  @Test
  void modify() {
    // given
    Long expenseId = 1L;
    Long amount = 10000L;
    String memo = "김치찌개";
    LocalDateTime expenseAt = LocalDateTime.now();

    Member member = new Member(1L);
    Category category = new Category(1L);
    MemberCategory memberCategory = new MemberCategory(member, category);

    Category newCategory = new Category(2L);
    MemberCategory newMemberCategory = new MemberCategory(member, newCategory);

    Expense expense = new Expense(expenseId, amount, memo, expenseAt, memberCategory);

    Long newAmount = 13000L;
    String newMemo = "갈비탕";
    LocalDateTime newExpenseAt = LocalDateTime.now();

    PatchExpense dto = new PatchExpense(newAmount, newMemo, newExpenseAt, 2L);

    // when
    expense.modify(dto, newMemberCategory);

    // then
    Assertions.assertThat(expense.getId()).isEqualTo(expenseId);
    Assertions.assertThat(expense.getAmount()).isEqualTo(newAmount);
    Assertions.assertThat(expense.getExpenseAt()).isEqualTo(newExpenseAt);
    Assertions.assertThat(expense.getMemberCategory().getCategory().getId()).isEqualTo(2L);
  }
}
