package com.qushe8r.expensemanager.budget.service;

import com.qushe8r.expensemanager.budget.dto.BudgetResponse;
import com.qushe8r.expensemanager.budget.dto.PatchBudget;
import com.qushe8r.expensemanager.budget.dto.PostBudget;
import com.qushe8r.expensemanager.budget.entity.Budget;
import com.qushe8r.expensemanager.budget.exception.BudgetAlreadyExistsException;
import com.qushe8r.expensemanager.budget.exception.BudgetNotFoundException;
import com.qushe8r.expensemanager.budget.mapper.BudgetMapper;
import com.qushe8r.expensemanager.budget.repository.BudgetRepository;
import com.qushe8r.expensemanager.category.entity.Category;
import com.qushe8r.expensemanager.category.entity.MemberCategory;
import com.qushe8r.expensemanager.matcher.BudgetMatcher;
import com.qushe8r.expensemanager.member.entity.Member;
import com.qushe8r.expensemanager.member.entity.MemberDetails;
import java.time.YearMonth;
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
    Budget budget = new Budget(expectedId, amount, month, new MemberCategory(member, category));

    PostBudget postBudget = new PostBudget(amount, month, categoryId);
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

  @DisplayName("createBudgetBudgetAlreadyExistsException(): 이미 예산이 존재하면 예외가 발생한다.")
  @Test
  void createBudgetBudgetAlreadyExistsException() {
    // given
    Long expectedId = 1L;
    Long categoryId = 1L;
    String categoryName = "카테고리";
    Long amount = 100000L;
    YearMonth month = YearMonth.of(2023, 11);

    Member member = new Member(1L);
    Category category = new Category(categoryId, categoryName);
    Budget rowBudget = new Budget(amount, month, new MemberCategory(member, category));
    Budget budget = new Budget(expectedId, amount, month, new MemberCategory(member, category));

    PostBudget postBudget = new PostBudget(amount, month, categoryId);
    MemberCategory memberCategory = new MemberCategory(member, new Category(1L));

    BDDMockito.given(budgetRepository.findByMonth(Mockito.eq(month)))
        .willReturn(Optional.of(budget));

    // when
    Assertions.assertThatThrownBy(() -> budgetService.createBudget(memberCategory, postBudget))
        .isInstanceOf(BudgetAlreadyExistsException.class);
    Mockito.verify(budgetRepository, Mockito.times(1)).findByMonth(Mockito.eq(month));
    Mockito.verify(budgetRepository, Mockito.times(0)).save(Mockito.any(Budget.class));
  }

  @DisplayName("modifyBudget(): 예산 수정이 완료되면 budgetResponse(dto)를 응답한다.")
  @Test
  void modifyBudget() {
    // given
    Long budgetId = 1L;
    Long amount = 150000L;
    YearMonth month = YearMonth.of(2023, 8);
    MemberDetails memberDetails = new MemberDetails(1L, "test@email.com", "");
    PatchBudget patchBudget = new PatchBudget(amount);

    BDDMockito.given(
            budgetRepository.findBudgetByIdAndMemberId(
                Mockito.eq(memberDetails.getId()), Mockito.eq(budgetId)))
        .willReturn(Optional.of(new Budget(budgetId, amount, month, null)));

    // when
    BudgetResponse response = budgetService.modifyBudget(memberDetails, budgetId, patchBudget);

    // then
    Assertions.assertThat(response)
        .hasFieldOrPropertyWithValue("budgetId", budgetId)
        .hasFieldOrPropertyWithValue("amount", amount)
        .hasFieldOrPropertyWithValue("month", month);
    Mockito.verify(budgetRepository, Mockito.times(1))
        .findBudgetByIdAndMemberId(Mockito.eq(memberDetails.getId()), Mockito.eq(budgetId));
  }

  @DisplayName("modifyBudgetBudgetNotFoundException(): 예산을 찾을 수 없습니다. 예외가 발생한다.")
  @Test
  void modifyBudgetBudgetNotFoundException() {
    // given
    Long budgetId = 1L;
    Long amount = 150000L;
    MemberDetails memberDetails = new MemberDetails(1L, "test@email.com", "");
    PatchBudget patchBudget = new PatchBudget(amount);

    BDDMockito.given(
            budgetRepository.findBudgetByIdAndMemberId(
                Mockito.eq(memberDetails.getId()), Mockito.eq(budgetId)))
        .willReturn(Optional.empty());

    // when & then
    Assertions.assertThatThrownBy(
            () -> budgetService.modifyBudget(memberDetails, budgetId, patchBudget))
        .isInstanceOf(BudgetNotFoundException.class);
    Mockito.verify(budgetRepository, Mockito.times(1))
        .findBudgetByIdAndMemberId(Mockito.eq(memberDetails.getId()), Mockito.eq(budgetId));
  }

  @DisplayName("deleteBudgetIfPresent(): 조건에 맞는 예산을 찾으면 삭제한다.")
  @Test
  void deleteBudget() {
    // given
    Long budgetId = 1L;
    MemberDetails memberDetails = new MemberDetails(1L, "test@email.com", "");

    BDDMockito.given(
            budgetRepository.findBudgetByIdAndMemberId(
                Mockito.eq(memberDetails.getId()), Mockito.eq(budgetId)))
        .willReturn(Optional.empty());

    // when
    budgetService.deleteBudget(memberDetails, budgetId);

    // then
    Mockito.verify(budgetRepository, Mockito.times(1))
        .findBudgetByIdAndMemberId(Mockito.eq(memberDetails.getId()), Mockito.eq(budgetId));
    Mockito.verify(budgetRepository, Mockito.times(0)).delete(Mockito.any(Budget.class));
  }

  @DisplayName("deleteBudgetIfPresent(): 조건에 맞는 예산을 찾으면 삭제한다.")
  @Test
  void deleteBudgetIfPresent() {
    // given
    Long budgetId = 1L;
    Long amount = 150000L;
    YearMonth month = YearMonth.of(2023, 8);
    MemberDetails memberDetails = new MemberDetails(1L, "test@email.com", "");

    Budget budget = new Budget(1L, amount, month, null);
    BDDMockito.given(
            budgetRepository.findBudgetByIdAndMemberId(
                Mockito.eq(memberDetails.getId()), Mockito.eq(budgetId)))
        .willReturn(Optional.of(budget));

    BDDMockito.doNothing()
        .when(budgetRepository)
        .delete(Mockito.argThat(new BudgetMatcher(budget)));

    // when
    budgetService.deleteBudget(memberDetails, budgetId);

    // then
    Mockito.verify(budgetRepository, Mockito.times(1))
        .findBudgetByIdAndMemberId(Mockito.eq(memberDetails.getId()), Mockito.eq(budgetId));
    Mockito.verify(budgetRepository, Mockito.times(1))
        .delete(Mockito.argThat(new BudgetMatcher(budget)));
  }
}
