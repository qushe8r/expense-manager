package com.qushe8r.expensemanager.expense.service;

import com.qushe8r.expensemanager.annotation.WithMemberPrincipals;
import com.qushe8r.expensemanager.category.entity.Category;
import com.qushe8r.expensemanager.category.entity.MemberCategory;
import com.qushe8r.expensemanager.expense.dto.ExpenseMonthlyReport;
import com.qushe8r.expensemanager.expense.dto.ExpenseReportResponse;
import com.qushe8r.expensemanager.expense.dto.ExpenseResponse;
import com.qushe8r.expensemanager.expense.dto.ExpenseWeeklyReport;
import com.qushe8r.expensemanager.expense.dto.PatchExpense;
import com.qushe8r.expensemanager.expense.dto.PostExpense;
import com.qushe8r.expensemanager.expense.entity.Expense;
import com.qushe8r.expensemanager.expense.exception.ExpenseNotFoundException;
import com.qushe8r.expensemanager.expense.mapper.ExpenseMapper;
import com.qushe8r.expensemanager.expense.repository.ExpenseQueryDslRepository;
import com.qushe8r.expensemanager.expense.repository.ExpenseReportRepository;
import com.qushe8r.expensemanager.expense.repository.ExpenseRepository;
import com.qushe8r.expensemanager.matcher.ExpenseMatcher;
import com.qushe8r.expensemanager.member.entity.Member;
import com.qushe8r.expensemanager.member.entity.MemberDetails;
import java.time.LocalDateTime;
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
class ExpenseServiceTest {

  @Spy private ExpenseMapper expenseMapper;

  @Mock private ExpenseRepository expenseRepository;

  @Mock private ExpenseQueryDslRepository expenseQueryDslRepository;

  @Mock private ExpenseReportRepository expenseReportRepository;

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
    ExpenseResponse result =
        expenseService.modifyExpense(memberId, memberCategory, expenseId, patchExpense);

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

  @DisplayName("getExpense(): 조회에 성공하면 response를 반환한다")
  @Test
  void getExpense() {
    // given
    Long memberId = 1L;
    Long expenseId = 1L;
    Long amount = 10000L;
    String memo = "김밥";
    LocalDateTime expenseAt = LocalDateTime.of(2023, 11, 15, 12, 0);

    String categoryName = "카테고리";

    Category category = new Category(1L, categoryName);
    MemberCategory memberCategory = new MemberCategory(1L, null, category);
    Expense expense = new Expense(expenseId, amount, memo, expenseAt, memberCategory);

    BDDMockito.given(expenseQueryDslRepository.query(memberId, expenseId))
        .willReturn(Optional.of(expense));

    // when
    ExpenseResponse result = expenseService.getExpense(memberId, expenseId);

    // then
    Assertions.assertThat(result)
        .hasFieldOrPropertyWithValue("expenseId", expenseId)
        .hasFieldOrPropertyWithValue("amount", amount)
        .hasFieldOrPropertyWithValue("memo", memo)
        .hasFieldOrPropertyWithValue("expenseAt", expenseAt)
        .hasFieldOrPropertyWithValue("categoryName", categoryName);
  }

  @DisplayName("getExpenseExpenseNotFoundException(): 조회 결과가 없으면 예외가 발생한다")
  @Test
  void getExpenseExpenseNotFoundException() {
    // given
    Long memberId = 1L;
    Long expenseId = 1L;

    BDDMockito.given(expenseQueryDslRepository.query(memberId, expenseId))
        .willReturn(Optional.empty());

    // when
    Assertions.assertThatThrownBy(() -> expenseService.getExpense(memberId, expenseId))
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

  @Test
  void getReport() {
    // given
    MemberDetails memberDetails = new MemberDetails(1L, "test@email.com", "");
    YearMonth thisMonth = YearMonth.now();
    YearMonth lastMonth = thisMonth.minusMonths(1);

    BDDMockito.given(
            expenseReportRepository.reportMonthlyExpenses(
                Mockito.eq(memberDetails.getId()),
                Mockito.any(LocalDateTime.class),
                Mockito.any(LocalDateTime.class),
                Mockito.eq(lastMonth)))
        .willReturn(List.of(new ExpenseMonthlyReport(1L, "categoryName", 100000L, 50000L)));

    BDDMockito.given(
            expenseReportRepository.reportMonthlyExpenses(
                Mockito.eq(memberDetails.getId()),
                Mockito.any(LocalDateTime.class),
                Mockito.any(LocalDateTime.class),
                Mockito.eq(thisMonth)))
        .willReturn(List.of(new ExpenseMonthlyReport(1L, "categoryName", 100000L, 35000L)));

    BDDMockito.given(
            expenseReportRepository.reportWeeklyExpenses(
                Mockito.eq(memberDetails.getId()),
                Mockito.any(LocalDateTime.class),
                Mockito.any(LocalDateTime.class),
                Mockito.any(LocalDateTime.class)))
        .willReturn(List.of(new ExpenseWeeklyReport(1L, "categoryName", 25000L, 10000L)));

    // when
    ExpenseReportResponse report = expenseService.getReport(memberDetails);

    // then
    Assertions.assertThat(report.monthlyReports()).hasSize(1);
    Assertions.assertThat(report.monthlyReports().get(0))
        .hasFieldOrPropertyWithValue("categoryId", 1L)
        .hasFieldOrPropertyWithValue("categoryName", "categoryName")
        .hasFieldOrPropertyWithValue("lastMonthBudget", 100000L)
        .hasFieldOrPropertyWithValue("lastMonthExpenseTotals", 50000L)
        .hasFieldOrPropertyWithValue("thisMonthBudget", 100000L)
        .hasFieldOrPropertyWithValue("thisMonthExpenseTotals", 35000L);
    Assertions.assertThat(report.weeklyReports()).hasSize(1);
    Assertions.assertThat(report.weeklyReports().get(0))
        .hasFieldOrPropertyWithValue("categoryId", 1L)
        .hasFieldOrPropertyWithValue("categoryName", "categoryName")
        .hasFieldOrPropertyWithValue("twoWeeksAgoExpenseAmount", 25000L)
        .hasFieldOrPropertyWithValue("oneWeekAgoExpenseAmount", 10000L);
  }
}
