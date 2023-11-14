package com.qushe8r.expensemanager.budget.service;

import com.qushe8r.expensemanager.budget.dto.PostBudget;
import com.qushe8r.expensemanager.category.entity.Category;
import com.qushe8r.expensemanager.category.entity.MemberCategory;
import com.qushe8r.expensemanager.category.service.CategoryService;
import com.qushe8r.expensemanager.category.service.MemberCategoryService;
import com.qushe8r.expensemanager.macher.MemberCategoryMatcher;
import com.qushe8r.expensemanager.macher.PostBudgetMatcher;
import com.qushe8r.expensemanager.member.entity.Member;
import com.qushe8r.expensemanager.member.entity.MemberDetails;
import java.time.YearMonth;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BudgetCreateUseCaseTest {

  @Mock private CategoryService categoryService;

  @Mock private MemberCategoryService memberCategoryService;

  @Mock private BudgetService budgetService;

  @InjectMocks private BudgetCreateUseCase budgetCreateUseCase;

  @Test
  void createBudget() {
    // given
    Long expectedId = 1L;
    Long categoryId = 1L;
    String categoryName = "카테고리";
    Long budgetAmount = 100000L;
    YearMonth month = YearMonth.of(2023, 11);
    PostBudget postBudget = new PostBudget(budgetAmount, month, categoryId);
    Category category = new Category(postBudget.categoryId(), categoryName);
    Member member = new Member(1L, "test@email.com", "");
    MemberCategory membercategory = new MemberCategory(1L, member, category);

    BDDMockito.given(categoryService.validateCategoryByIdOrElseThrow(postBudget.categoryId()))
        .willReturn(category);

    BDDMockito.given(memberCategoryService.findByMemberCategoryOrElseSave(1L, 1L))
        .willReturn(membercategory);

    BDDMockito.given(
            budgetService.createBudget(
                Mockito.argThat(new MemberCategoryMatcher(membercategory)),
                Mockito.argThat(new PostBudgetMatcher(postBudget))))
        .willReturn(expectedId);

    MemberDetails memberDetails = new MemberDetails(1L, "test@email.com", "password");
    // when
    Long actualId = budgetCreateUseCase.createBudget(memberDetails, postBudget);

    // then
    Assertions.assertThat(actualId).isEqualTo(expectedId);
  }
}
