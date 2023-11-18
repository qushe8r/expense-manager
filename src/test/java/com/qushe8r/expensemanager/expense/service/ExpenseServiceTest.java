package com.qushe8r.expensemanager.expense.service;

import com.qushe8r.expensemanager.annotation.WithMemberPrincipals;
import com.qushe8r.expensemanager.category.entity.Category;
import com.qushe8r.expensemanager.category.entity.MemberCategory;
import com.qushe8r.expensemanager.expense.dto.ExpenseResponse;
import com.qushe8r.expensemanager.expense.dto.PatchExpense;
import com.qushe8r.expensemanager.expense.dto.PostExpense;
import com.qushe8r.expensemanager.expense.entity.Expense;
import com.qushe8r.expensemanager.expense.exception.ExpenseNotFoundException;
import com.qushe8r.expensemanager.expense.mapper.ExpenseMapper;
import com.qushe8r.expensemanager.expense.repository.ExpenseRepository;
import com.qushe8r.expensemanager.matcher.ExpenseMatcher;
import com.qushe8r.expensemanager.member.entity.Member;
import java.time.LocalDateTime;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ExpenseServiceTest {

  @Spy private ExpenseMapper expenseMapper;

  @Mock private ExpenseRepository expenseRepository;

  @InjectMocks private ExpenseService expenseService;

  @DisplayName("createBudget(): 지출 입력이 완료되면 기록된 지출 id를 응답한다.")
  @Test
  void createBudget() {
    Long expectedId = 1L;
    Long categoryId = 1L;
    String categoryName = "카테고리";
    Long expenseAmount = 100000L;
    String memo = "돼지국밥";
    LocalDateTime expenseAt = LocalDateTime.of(2023, 11, 15, 12, 0);

    PostExpense postExpense = new PostExpense(expenseAmount, memo, expenseAt, categoryId);

    Member member = new Member(1L);
    Category category = new Category(categoryId, categoryName);
    MemberCategory memberCategory = new MemberCategory(member, category);

    Expense rowExpense = new Expense(expenseAmount, memo, expenseAt, memberCategory);
    Expense expense = new Expense(expectedId, expenseAmount, memo, expenseAt, memberCategory);

    BDDMockito.given(expenseRepository.save(Mockito.argThat(new ExpenseMatcher(rowExpense))))
        .willReturn(expense);

    // when
    Long budgetId = expenseService.createExpense(memberCategory, postExpense);

    // then
    Assertions.assertThat(budgetId).isEqualTo(expectedId);
    Mockito.verify(expenseRepository, Mockito.times(1))
        .save(Mockito.argThat(new ExpenseMatcher(rowExpense)));
  }

  @DisplayName("modifyExpense(): 수정에 성공하면 수정된 정보를 응답한다.")
  @Test
  void modifyExpense() {
    // given
    Long expenseId = 1L;
    Long memberId = 1L;
    Long categoryId = 1L;
    Long amount = 12000L;
    String memo = "김치찌개";
    LocalDateTime expenseAt = LocalDateTime.of(2023, 11, 15, 12, 0);
    String categoryName = "카테고리";

    MemberCategory memberCategory =
        new MemberCategory(categoryId, new Member(1L), new Category(1L, categoryName));
    PatchExpense patchExpense = new PatchExpense(amount, memo, expenseAt, categoryId);

    BDDMockito.given(expenseRepository.findExpenseMemberCategoryById(memberId, expenseId))
        .willReturn(
            Optional.of(
                new Expense(
                    expenseId,
                    10000L,
                    "돼지국밥",
                    LocalDateTime.of(2023, 11, 13, 12, 0),
                    memberCategory)));

    // when
    ExpenseResponse result = expenseService.modifyExpense(memberId, memberCategory, expenseId, patchExpense);

    // then
    Assertions.assertThat(result)
        .hasFieldOrPropertyWithValue("expenseId", expenseId)
        .hasFieldOrPropertyWithValue("amount", amount)
        .hasFieldOrPropertyWithValue("memo", memo)
        .hasFieldOrPropertyWithValue("expenseAt", expenseAt)
        .hasFieldOrPropertyWithValue("categoryName", categoryName);
  }

  @DisplayName("modifyExpenseExpenseNotFoundException(): 지출이 없으면 ExpenseNotFoundException이 발생한다.")
  @Test
  void modifyExpenseExpenseNotFoundException() {
    // given
    Long expenseId = 1L;
    Long memberId = 1L;
    Long categoryId = 1L;
    Long amount = 12000L;
    String memo = "김치찌개";
    LocalDateTime expenseAt = LocalDateTime.of(2023, 11, 15, 12, 0);
    String categoryName = "카테고리";

    MemberCategory memberCategory =
        new MemberCategory(categoryId, new Member(1L), new Category(1L, categoryName));
    PatchExpense patchExpense = new PatchExpense(amount, memo, expenseAt, categoryId);

    BDDMockito.given(expenseRepository.findExpenseMemberCategoryById(memberId, expenseId))
        .willReturn(Optional.empty());

    // when
    Assertions.assertThatThrownBy(
            () -> expenseService.modifyExpense(memberId, memberCategory, expenseId, patchExpense))
        .isInstanceOf(ExpenseNotFoundException.class);
  }

  @DisplayName("deleteExpense(): 입력값이 유효하면 삭제한다.")
  @WithMemberPrincipals
  @Test
  void deleteExpense() {
    // given
    Long memberId = 1L;
    Long expenseId = 5L;

    MemberCategory memberCategory = new MemberCategory(3L, new Member(memberId), new Category(4L));
    Expense expense =
        new Expense(1L, 10000L, "돼지국밥", LocalDateTime.of(2023, 11, 15, 12, 0), memberCategory);

    BDDMockito.given(expenseRepository.findByMemberIdAndExpenseId(memberId, expenseId))
        .willReturn(Optional.of(expense));

    BDDMockito.doNothing().when(expenseRepository).delete(expense);

    // when
    expenseService.deleteExpense(memberId, expenseId);
    Mockito.verify(expenseRepository, Mockito.times(1))
        .findByMemberIdAndExpenseId(memberId, expenseId);
    Mockito.verify(expenseRepository, Mockito.times(1)).delete(expense);
  }

  @DisplayName("deleteExpenseDoNoting(): 입력값이 유효하지 않으면 아무것도 하지 않는다.")
  @WithMemberPrincipals
  @Test
  void deleteExpenseDoNoting() {
    // given
    Long memberId = 1L;
    Long expenseId = 5L;

    BDDMockito.given(expenseRepository.findByMemberIdAndExpenseId(memberId, expenseId))
        .willReturn(Optional.empty());

    // when
    expenseService.deleteExpense(memberId, expenseId);

    // then
    Mockito.verify(expenseRepository, Mockito.times(1))
        .findByMemberIdAndExpenseId(memberId, expenseId);
    Mockito.verify(expenseRepository, Mockito.times(0)).delete(Mockito.any(Expense.class));
  }
}
