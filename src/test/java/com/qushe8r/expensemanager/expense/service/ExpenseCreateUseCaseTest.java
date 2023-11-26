package com.qushe8r.expensemanager.expense.service;

import com.qushe8r.expensemanager.category.entity.Category;
import com.qushe8r.expensemanager.category.entity.MemberCategory;
import com.qushe8r.expensemanager.category.service.CategoryService;
import com.qushe8r.expensemanager.category.service.MemberCategoryService;
import com.qushe8r.expensemanager.expense.dto.PostExpense;
import com.qushe8r.expensemanager.matcher.MemberCategoryMatcher;
import com.qushe8r.expensemanager.matcher.PostExpenseMatcher;
import com.qushe8r.expensemanager.member.entity.Member;
import com.qushe8r.expensemanager.member.entity.MemberDetails;
import java.time.LocalDateTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ExpenseCreateUseCaseTest {

  @Mock private CategoryService categoryService;

  @Mock private MemberCategoryService memberCategoryService;

  @Mock private ExpenseService expenseService;

  @InjectMocks private ExpenseCreateUseCase expenseCreateUseCase;

  @Test
  void createBudget() {
    // given
    Long expectedId = 1L;

    MemberDetails memberDetails = new MemberDetails(1L, "test@email.com", "password");
    Long expenseAmount = 100000L;
    String memo = "돼지국밥";
    LocalDateTime expenseAt = LocalDateTime.of(2023, 11, 15, 12, 0);
    Long categoryId = 1L;
    String categoryName = "카테고리";

    PostExpense postExpense = new PostExpense(expenseAmount, memo, expenseAt, categoryId);
    Category category = new Category(postExpense.categoryId(), categoryName);
    Member member = new Member(1L, "test@email.com", "", false, false);
    MemberCategory membercategory = new MemberCategory(1L, member, category);

    BDDMockito.given(categoryService.validateCategoryByIdOrElseThrow(postExpense.categoryId()))
        .willReturn(category);

    BDDMockito.given(memberCategoryService.findByMemberCategoryOrElseSave(1L, 1L))
        .willReturn(membercategory);

    BDDMockito.given(
            expenseService.createExpense(
                Mockito.argThat(new MemberCategoryMatcher(membercategory)),
                Mockito.argThat(new PostExpenseMatcher(postExpense))))
        .willReturn(expectedId);

    // when
    Long actualId = expenseCreateUseCase.createExpense(memberDetails, postExpense);

    // then
    Assertions.assertThat(actualId).isEqualTo(expectedId);
  }
}
