package com.qushe8r.expensemanager.expense.service;

import com.qushe8r.expensemanager.category.entity.Category;
import com.qushe8r.expensemanager.category.entity.MemberCategory;
import com.qushe8r.expensemanager.expense.dto.PostExpense;
import com.qushe8r.expensemanager.expense.entity.Expense;
import com.qushe8r.expensemanager.expense.mapper.ExpenseMapper;
import com.qushe8r.expensemanager.expense.repository.ExpenseRepository;
import com.qushe8r.expensemanager.matcher.ExpenseMatcher;
import com.qushe8r.expensemanager.member.entity.Member;
import java.time.LocalDateTime;
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
}
