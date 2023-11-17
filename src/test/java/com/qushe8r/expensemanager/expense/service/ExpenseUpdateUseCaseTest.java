package com.qushe8r.expensemanager.expense.service;

import com.qushe8r.expensemanager.category.entity.Category;
import com.qushe8r.expensemanager.category.entity.MemberCategory;
import com.qushe8r.expensemanager.category.service.MemberCategoryService;
import com.qushe8r.expensemanager.expense.dto.ExpenseResponse;
import com.qushe8r.expensemanager.expense.dto.PatchExpense;
import com.qushe8r.expensemanager.matcher.MemberCategoryMatcher;
import com.qushe8r.expensemanager.matcher.PatchExpenseMatcher;
import com.qushe8r.expensemanager.member.entity.Member;
import com.qushe8r.expensemanager.member.entity.MemberDetails;
import java.time.LocalDateTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ExpenseUpdateUseCaseTest {

  @Mock private MemberCategoryService memberCategoryService;

  @Mock private ExpenseService expenseService;

  @InjectMocks private ExpenseUpdateUseCase expenseUpdateUseCase;

  @DisplayName("modifyBudget(): 정상 입")
  @Test
  void modifyBudget() {
    // given
    Long expenseId = 1L;

    MemberDetails memberDetails = new MemberDetails(1L, "test@email.com", "password");
    Long amount = 100000L;
    String memo = "돼지국밥";
    LocalDateTime expenseAt = LocalDateTime.of(2023, 11, 15, 12, 0);
    Long memberId = 1L;
    Long categoryId = 1L;
    String categoryName = "카테고리";

    PatchExpense patchExpense = new PatchExpense(amount, memo, expenseAt, categoryId);
    Category category = new Category(patchExpense.categoryId(), categoryName);
    Member member = new Member(1L, "test@email.com", "");
    MemberCategory membercategory = new MemberCategory(memberId, member, category);

    BDDMockito.given(memberCategoryService.findByMemberCategoryOrElseSave(memberId, categoryId))
        .willReturn(membercategory);

    BDDMockito.given(
            expenseService.modifyExpense(
                Mockito.argThat(new MemberCategoryMatcher(membercategory)),
                Mockito.eq(expenseId),
                Mockito.argThat(new PatchExpenseMatcher(patchExpense))))
        .willReturn(new ExpenseResponse(expenseId, amount, memo, expenseAt, categoryName));

    // when
    ExpenseResponse result =
        expenseUpdateUseCase.modifyExpense(memberDetails, expenseId, patchExpense);

    // then
    Assertions.assertThat(result)
        .hasFieldOrPropertyWithValue("expenseId", expenseId)
        .hasFieldOrPropertyWithValue("amount", amount)
        .hasFieldOrPropertyWithValue("memo", memo)
        .hasFieldOrPropertyWithValue("expenseAt", expenseAt)
        .hasFieldOrPropertyWithValue("categoryName", categoryName);
    Mockito.verify(memberCategoryService, Mockito.times(1))
        .findByMemberCategoryOrElseSave(memberId, categoryId);
    Mockito.verify(expenseService, Mockito.times(1))
        .modifyExpense(
            Mockito.argThat(new MemberCategoryMatcher(membercategory)),
            Mockito.eq(expenseId),
            Mockito.argThat(new PatchExpenseMatcher(patchExpense)));
  }
}
