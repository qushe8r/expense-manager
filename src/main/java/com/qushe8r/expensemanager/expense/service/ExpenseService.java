package com.qushe8r.expensemanager.expense.service;

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
import com.qushe8r.expensemanager.member.entity.MemberDetails;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ExpenseService {

  private final ExpenseMapper expenseMapper;

  private final ExpenseRepository expenseRepository;

  private final ExpenseQueryDslRepository expenseQueryDslRepository;

  private final ExpenseReportRepository expenseReportRepository;

  @Transactional
  public Long createExpense(MemberCategory memberCategory, PostExpense dto) {
    Expense rowBudget = expenseMapper.toEntity(memberCategory, dto);
    Expense budget = expenseRepository.save(rowBudget);
    return budget.getId();
  }

  @Transactional
  public ExpenseResponse modifyExpense(
      Long memberId, MemberCategory memberCategory, Long expenseId, PatchExpense dto) {
    Expense expense =
        expenseRepository
            .findExpenseMemberCategoryById(memberId, expenseId)
            .orElseThrow(ExpenseNotFoundException::new);
    expense.modify(dto, memberCategory);
    return expenseMapper.toResponse(expense);
  }

  public ExpenseResponse getExpense(Long memberId, Long expenseId) {
    Expense expense =
        expenseQueryDslRepository
            .query(memberId, expenseId)
            .orElseThrow(ExpenseNotFoundException::new);
    return expenseMapper.toResponse(expense);
  }

  @Transactional
  public void deleteExpense(Long memberId, Long expenseId) {
    expenseRepository
        .findByMemberIdAndExpenseId(memberId, expenseId)
        .ifPresent(expenseRepository::delete);
  }

  public ExpenseReportResponse getReport(MemberDetails memberDetails) {
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime baseDate = now.toLocalDate().atTime(LocalTime.MIN);
    LocalDateTime startDateOfThisMonth = baseDate.with(TemporalAdjusters.firstDayOfMonth());
    YearMonth thisMonth = YearMonth.now();

    LocalDateTime startDateOfOneMonthAgo = startDateOfThisMonth.minusMonths(1);
    LocalDateTime dateOfOneMonthAgo = now.minusMonths(1);
    YearMonth lastMonth = thisMonth.minusMonths(1);

    List<ExpenseMonthlyReport> lastMonthReports =
        expenseReportRepository.reportMonthlyExpenses(
            memberDetails.getId(), startDateOfOneMonthAgo, dateOfOneMonthAgo, lastMonth);

    List<ExpenseMonthlyReport> thisMonthReports =
        expenseReportRepository.reportMonthlyExpenses(
            memberDetails.getId(), startDateOfThisMonth, baseDate, thisMonth);

    LocalDateTime oneWeekAgoDate = baseDate.minusDays(7);
    LocalDateTime twoWeeksAgoDate = baseDate.minusDays(14);

    List<ExpenseWeeklyReport> expenseWeeklyReports =
        expenseReportRepository.reportWeeklyExpenses(
            memberDetails.getId(), twoWeeksAgoDate, oneWeekAgoDate, now);

    return expenseMapper.toResponse(lastMonthReports, thisMonthReports, expenseWeeklyReports);
  }
}
