package com.qushe8r.expensemanager.budget.service;

import com.qushe8r.expensemanager.budget.dto.BudgetRecommendationRate;
import com.qushe8r.expensemanager.budget.dto.BudgetRecommendationResponse;
import com.qushe8r.expensemanager.budget.dto.BudgetResponse;
import com.qushe8r.expensemanager.budget.dto.PatchBudget;
import com.qushe8r.expensemanager.budget.dto.PostBudget;
import com.qushe8r.expensemanager.budget.entity.Budget;
import com.qushe8r.expensemanager.budget.exception.BudgetAlreadyExistsException;
import com.qushe8r.expensemanager.budget.exception.BudgetNotFoundException;
import com.qushe8r.expensemanager.budget.mapper.BudgetMapper;
import com.qushe8r.expensemanager.budget.repository.BudgetRecommendationRepository;
import com.qushe8r.expensemanager.budget.repository.BudgetRepository;
import com.qushe8r.expensemanager.category.entity.Category;
import com.qushe8r.expensemanager.category.entity.MemberCategory;
import com.qushe8r.expensemanager.matcher.BudgetMatcher;
import com.qushe8r.expensemanager.member.entity.Member;
import com.qushe8r.expensemanager.member.entity.MemberDetails;
import java.time.YearMonth;
import java.util.List;
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

  @Mock private BudgetRecommendationRepository budgetRecommendationRepository;

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
    Budget budget = new Budget(expectedId, amount, month, new MemberCategory(member, category));

    PostBudget postBudget = new PostBudget(amount, month, categoryId);
    MemberCategory memberCategory = new MemberCategory(1L, member, new Category(1L));

    BDDMockito.given(budgetRepository.findByMemberIdAndMonth(memberCategory.getId(), month))
        .willReturn(Optional.of(budget));

    // when
    Assertions.assertThatThrownBy(() -> budgetService.createBudget(memberCategory, postBudget))
        .isInstanceOf(BudgetAlreadyExistsException.class);
    Mockito.verify(budgetRepository, Mockito.times(1))
        .findByMemberIdAndMonth(memberCategory.getId(), month);
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

    BDDMockito.given(budgetRepository.findBudgetByIdAndMemberId(memberDetails.getId(), budgetId))
        .willReturn(Optional.of(new Budget(budgetId, amount, month, null)));

    // when
    BudgetResponse response = budgetService.modifyBudget(memberDetails, budgetId, patchBudget);

    // then
    Assertions.assertThat(response)
        .hasFieldOrPropertyWithValue("budgetId", budgetId)
        .hasFieldOrPropertyWithValue("amount", amount)
        .hasFieldOrPropertyWithValue("month", month);
    Mockito.verify(budgetRepository, Mockito.times(1))
        .findBudgetByIdAndMemberId(memberDetails.getId(), budgetId);
  }

  @DisplayName("modifyBudgetBudgetNotFoundException(): 예산을 찾을 수 없습니다. 예외가 발생한다.")
  @Test
  void modifyBudgetBudgetNotFoundException() {
    // given
    Long budgetId = 1L;
    Long amount = 150000L;
    MemberDetails memberDetails = new MemberDetails(1L, "test@email.com", "");
    PatchBudget patchBudget = new PatchBudget(amount);

    BDDMockito.given(budgetRepository.findBudgetByIdAndMemberId(memberDetails.getId(), budgetId))
        .willReturn(Optional.empty());

    // when & then
    Assertions.assertThatThrownBy(
            () -> budgetService.modifyBudget(memberDetails, budgetId, patchBudget))
        .isInstanceOf(BudgetNotFoundException.class);
    Mockito.verify(budgetRepository, Mockito.times(1))
        .findBudgetByIdAndMemberId(memberDetails.getId(), budgetId);
  }

  @DisplayName("deleteBudgetIfPresent(): 조건에 맞는 예산을 찾으면 삭제한다.")
  @Test
  void deleteBudget() {
    // given
    Long budgetId = 1L;
    MemberDetails memberDetails = new MemberDetails(1L, "test@email.com", "");

    BDDMockito.given(budgetRepository.findBudgetByIdAndMemberId(memberDetails.getId(), budgetId))
        .willReturn(Optional.empty());

    // when
    budgetService.deleteBudget(memberDetails, budgetId);

    // then
    Mockito.verify(budgetRepository, Mockito.times(1))
        .findBudgetByIdAndMemberId(memberDetails.getId(), budgetId);
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
    BDDMockito.given(budgetRepository.findBudgetByIdAndMemberId(memberDetails.getId(), budgetId))
        .willReturn(Optional.of(budget));

    BDDMockito.doNothing()
        .when(budgetRepository)
        .delete(Mockito.argThat(new BudgetMatcher(budget)));

    // when
    budgetService.deleteBudget(memberDetails, budgetId);

    // then
    Mockito.verify(budgetRepository, Mockito.times(1))
        .findBudgetByIdAndMemberId(memberDetails.getId(), budgetId);
    Mockito.verify(budgetRepository, Mockito.times(1))
        .delete(Mockito.argThat(new BudgetMatcher(budget)));
  }

  @Test
  void getRecommendation() {
    // given
    Long amount = 1000000L;
    List<BudgetRecommendationRate> rates =
        List.of(
            new BudgetRecommendationRate("카테고리1", 0.3),
            new BudgetRecommendationRate("카테고리2", 0.25),
            new BudgetRecommendationRate("카테고리3", 0.08),
            new BudgetRecommendationRate("카테고리4", 0.20),
            new BudgetRecommendationRate("카테고리5", 0.02),
            new BudgetRecommendationRate("카테고리6", 0.07),
            new BudgetRecommendationRate("기타", 0.08));

    BDDMockito.given(budgetRecommendationRepository.getRecommendation()).willReturn(rates);

    // when
    List<BudgetRecommendationResponse> result = budgetService.getRecommendation(amount);

    // then
    Assertions.assertThat(result).size().isEqualTo(4);
    Assertions.assertThat(result.get(0))
        .hasFieldOrPropertyWithValue("categoryName", "카테고리1")
        .hasFieldOrPropertyWithValue("amount", (long) (amount * 0.3));
    Assertions.assertThat(result.get(1))
        .hasFieldOrPropertyWithValue("categoryName", "카테고리2")
        .hasFieldOrPropertyWithValue("amount", (long) (amount * 0.25));
    Assertions.assertThat(result.get(2))
        .hasFieldOrPropertyWithValue("categoryName", "카테고리4")
        .hasFieldOrPropertyWithValue("amount", (long) (amount * 0.2));
    Assertions.assertThat(result.get(3))
        .hasFieldOrPropertyWithValue("categoryName", "기타")
        .hasFieldOrPropertyWithValue("amount", (long) (amount * 0.25));
  }
}
