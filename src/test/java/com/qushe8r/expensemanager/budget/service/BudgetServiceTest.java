package com.qushe8r.expensemanager.budget.service;

import com.qushe8r.expensemanager.budget.dto.PostBudget;
import com.qushe8r.expensemanager.budget.entity.Budget;
import com.qushe8r.expensemanager.budget.mapper.BudgetMapper;
import com.qushe8r.expensemanager.budget.repository.BudgetRepository;
import com.qushe8r.expensemanager.category.entity.Category;
import com.qushe8r.expensemanager.category.entity.MemberCategory;
import com.qushe8r.expensemanager.macher.BudgetMatcher;
import com.qushe8r.expensemanager.member.entity.Member;
import java.time.YearMonth;
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
class BudgetServiceTest {

  @Spy private BudgetMapper budgetMapper;

  @Mock private BudgetRepository budgetRepository;

  @InjectMocks private BudgetService budgetService;

  @DisplayName("createBudget(): 예산 입력이 완료되면 가입된 예산 id를 응답한다.")
  @Test
  void createBudget() {
    // given
    Long expectedId = 1L;
    Long categoryId = 1L;
    String categoryName = "카테고리";
    Long amount = 100000L;
    YearMonth month = YearMonth.of(2023, 11);

    Member member = new Member(1L);
    Category category = new Category(categoryId, categoryName);
    Budget rowBudget = new Budget(amount, month, new MemberCategory(member, category));
    Budget budget =
        new Budget(expectedId, amount, month, new MemberCategory(member, category));

    PostBudget postBudget =
        new PostBudget(amount, month, categoryId);
    MemberCategory memberCategory = new MemberCategory(member, new Category(1L));

    BDDMockito.given(budgetRepository.save(Mockito.argThat(new BudgetMatcher(rowBudget))))
        .willReturn(budget);

    // when
    Long budgetId = budgetService.createBudget(memberCategory, postBudget);

    // then
    Assertions.assertThat(budgetId).isEqualTo(expectedId);
    Mockito.verify(budgetRepository, Mockito.times(1))
        .save(Mockito.argThat(new BudgetMatcher(rowBudget)));
  }
}
