package com.qushe8r.expensemanager.category.service;

import com.qushe8r.expensemanager.category.dto.CategoryTotalsExpenseResponse;
import com.qushe8r.expensemanager.category.dto.GlobalTotalsExpenseResponse;
import com.qushe8r.expensemanager.category.entity.Category;
import com.qushe8r.expensemanager.category.entity.MemberCategory;
import com.qushe8r.expensemanager.category.mapper.MemberCategoryMapper;
import com.qushe8r.expensemanager.category.repository.MemberCategoryRepository;
import com.qushe8r.expensemanager.common.exception.ValidateDurationException;
import com.qushe8r.expensemanager.common.exception.ValidateEndBeforeStartException;
import com.qushe8r.expensemanager.common.exception.ValidateRangeException;
import com.qushe8r.expensemanager.expense.dto.CategorylessExpenseResponse;
import com.qushe8r.expensemanager.expense.entity.Expense;
import com.qushe8r.expensemanager.matcher.MemberCategoryMatcher;
import com.qushe8r.expensemanager.member.entity.Member;
import com.qushe8r.expensemanager.member.entity.MemberDetails;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
class MemberCategoryServiceTest {

  @Spy private MemberCategoryMapper memberCategoryMapper;

  @Mock private MemberCategoryRepository memberCategoryRepository;

  @InjectMocks private MemberCategoryService memberCategoryService;

  @DisplayName("findByMemberCategoryOrElseSave_Find(): memberCategory 조회에 성공하면 반환한다.")
  @Test
  void findByMemberCategoryOrElseSave_Find() {
    // given
    Long memberId = 1L;
    Long categoryId = 3L;

    MemberCategory memberCategory =
        new MemberCategory(1L, new Member(memberId), new Category(categoryId));
    BDDMockito.given(memberCategoryRepository.findByMemberIdAndCategoryId(memberId, categoryId))
        .willReturn(Optional.of(memberCategory));

    // when
    MemberCategory result =
        memberCategoryService.findByMemberCategoryOrElseSave(memberId, categoryId);

    // then
    Assertions.assertThat(result).isEqualTo(memberCategory);
  }

  @DisplayName("findByMemberCategoryOrElseSave_Save(): memberCategory 조회에 실패하면 저장한다.")
  @Test
  void findByMemberCategoryOrElseSave_Save() {
    // given
    Long memberId = 1L;
    Long categoryId = 3L;
    MemberCategory rowMemberCategory =
        new MemberCategory(new Member(memberId), new Category(categoryId));
    MemberCategory memberCategory =
        new MemberCategory(1L, new Member(memberId), new Category(categoryId));

    BDDMockito.given(memberCategoryRepository.findByMemberIdAndCategoryId(memberId, categoryId))
        .willReturn(Optional.empty());

    BDDMockito.given(
            memberCategoryRepository.save(
                Mockito.argThat(new MemberCategoryMatcher(rowMemberCategory))))
        .willReturn(memberCategory);

    // when
    MemberCategory result =
        memberCategoryService.findByMemberCategoryOrElseSave(memberId, categoryId);

    // then
    Assertions.assertThat(result).isEqualTo(memberCategory);
    Mockito.verify(memberCategoryRepository, Mockito.times(1))
        .findByMemberIdAndCategoryId(memberId, categoryId);
    Mockito.verify(memberCategoryRepository, Mockito.times(1))
        .save(Mockito.argThat(new MemberCategoryMatcher(rowMemberCategory)));
  }

  @DisplayName("getCategorizedExpense(): 조회에 성공하면 GlobalTotalsExpenseResponse(dto)를 응답한다.")
  @Test
  void getCategorizedExpense() {
    // given
    LocalDate start = LocalDate.of(2023, 11, 1);
    LocalDate end = LocalDate.of(2023, 11, 30);
    Long min = 10000L;
    Long max = 50000L;
    Category category = new Category(1L, "식사");
    String memo = "김치찌개";
    Long amount = 10000L;
    Long expenseId = 1L;
    LocalDateTime expenseAt = LocalDateTime.of(2023, 11, 15, 12, 0);

    MemberDetails memberDetails = new MemberDetails(1L, "test@email.com", "");
    Member member = new Member(1L, "test@email.com", "");
    Expense expense = new Expense(expenseId, amount, memo, expenseAt, null);
    MemberCategory memberCategory = new MemberCategory(1L, member, category, List.of(expense));

    BDDMockito.given(
            memberCategoryRepository.findCategoriesByAmountRangeAndDateRange(
                Mockito.eq(memberDetails.getId()),
                Mockito.eq(start.atTime(LocalTime.MIN)),
                Mockito.eq(end.atTime(LocalTime.MAX)),
                Mockito.eq(min),
                Mockito.eq(max)))
        .willReturn(List.of(memberCategory));

    // when
    GlobalTotalsExpenseResponse result =
        memberCategoryService.getCategorizedExpense(memberDetails, start, end, min, max);

    // then
    List<CategoryTotalsExpenseResponse> categories = result.categories();
    List<CategorylessExpenseResponse> expenses = result.categories().get(0).expenses();

    Assertions.assertThat(result)
        .hasFieldOrPropertyWithValue("globalTotals", 10000L)
        .hasFieldOrProperty("categories");
    Assertions.assertThat(categories).isNotEmpty();
    Assertions.assertThat(categories.get(0))
        .hasFieldOrPropertyWithValue("categoryName", "식사")
        .hasFieldOrPropertyWithValue("categoryTotals", 10000L);
    Assertions.assertThat(expenses).isNotEmpty();
    Assertions.assertThat(expenses.get(0))
        .hasFieldOrPropertyWithValue("expenseId", expenseId)
        .hasFieldOrPropertyWithValue("amount", amount)
        .hasFieldOrPropertyWithValue("memo", memo)
        .hasFieldOrPropertyWithValue("expenseAt", expenseAt);
    Mockito.verify(memberCategoryRepository, Mockito.times(1))
        .findCategoriesByAmountRangeAndDateRange(
            Mockito.eq(memberDetails.getId()),
            Mockito.eq(start.atTime(LocalTime.MIN)),
            Mockito.eq(end.atTime(LocalTime.MAX)),
            Mockito.eq(min),
            Mockito.eq(max));
  }

  @DisplayName("getCategorizedExpenseValidateEndBeforeStartException(): 날자 유효성 검사 실패")
  @Test
  void getCategorizedExpenseValidateEndBeforeStartException() {
    // given
    LocalDate start = LocalDate.of(2023, 11, 30);
    LocalDate end = LocalDate.of(2023, 11, 1);
    Long min = 10000L;
    Long max = 50000L;
    MemberDetails memberDetails = new MemberDetails(1L, "test@email.com", "");

    // when & then
    Assertions.assertThatThrownBy(
            () -> memberCategoryService.getCategorizedExpense(memberDetails, start, end, min, max))
        .isInstanceOf(ValidateEndBeforeStartException.class);
  }

  @DisplayName("getCategorizedExpenseValidateDurationException(): 날자 유효성 검사 실패")
  @Test
  void getCategorizedExpenseValidateDurationException() {
    // given
    LocalDate start = LocalDate.of(2023, 11, 1);
    LocalDate end = LocalDate.of(2023, 12, 10);
    Long min = 10000L;
    Long max = 50000L;
    MemberDetails memberDetails = new MemberDetails(1L, "test@email.com", "");

    // when & then
    Assertions.assertThatThrownBy(
            () -> memberCategoryService.getCategorizedExpense(memberDetails, start, end, min, max))
        .isInstanceOf(ValidateDurationException.class);
  }

  @DisplayName("getCategorizedExpenseValidateRangeException(): min, max 유효성 검사 실패")
  @Test
  void getCategorizedExpenseValidateRangeException() {
    // given
    LocalDate start = LocalDate.of(2023, 11, 1);
    LocalDate end = LocalDate.of(2023, 11, 30);
    Long min = 50000L;
    Long max = 10000L;
    MemberDetails memberDetails = new MemberDetails(1L, "test@email.com", "");

    // when & then
    Assertions.assertThatThrownBy(
            () -> memberCategoryService.getCategorizedExpense(memberDetails, start, end, min, max))
        .isInstanceOf(ValidateRangeException.class);
  }
}
