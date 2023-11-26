package com.qushe8r.expensemanager.expense.mapper;

import com.qushe8r.expensemanager.category.entity.MemberCategory;
import com.qushe8r.expensemanager.expense.dto.CategorylessExpenseResponse;
import com.qushe8r.expensemanager.expense.dto.ExpenseMonthlyReport;
import com.qushe8r.expensemanager.expense.dto.ExpenseMonthlyReportResponse;
import com.qushe8r.expensemanager.expense.dto.ExpenseReportResponse;
import com.qushe8r.expensemanager.expense.dto.ExpenseResponse;
import com.qushe8r.expensemanager.expense.dto.ExpenseWeeklyReport;
import com.qushe8r.expensemanager.expense.dto.PostExpense;
import com.qushe8r.expensemanager.expense.entity.Expense;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ExpenseMapper {

  public Expense toEntity(MemberCategory memberCategory, PostExpense dto) {
    return new Expense(dto.amount(), dto.memo(), dto.expenseAt(), memberCategory);
  }

  public ExpenseResponse toResponse(Expense expense) {
    return new ExpenseResponse(
        expense.getId(),
        expense.getAmount(),
        expense.getMemo(),
        expense.getExpenseAt(),
        expense.getMemberCategory().getCategory().getName());
  }

  public static CategorylessExpenseResponse toCategorylessExpenseResponse(Expense expense) {
    return new CategorylessExpenseResponse(
        expense.getId(), expense.getAmount(), expense.getMemo(), expense.getExpenseAt());
  }

  public ExpenseReportResponse toResponse(
      List<ExpenseMonthlyReport> lastMonthReports,
      List<ExpenseMonthlyReport> thisMonthReports,
      List<ExpenseWeeklyReport> weeklyReports) {

    List<ExpenseMonthlyReportResponse> monthlyReportResponses = new ArrayList<>();

    for (int i = 0; i < thisMonthReports.size(); i++) {
      ExpenseMonthlyReport lastMonthReport = lastMonthReports.get(i);
      ExpenseMonthlyReport thisMonthReport = thisMonthReports.get(i);

      ExpenseMonthlyReportResponse monthlyReportResponse =
          new ExpenseMonthlyReportResponse(
              lastMonthReport.categoryId(),
              lastMonthReport.categoryName(),
              lastMonthReport.budgetAmount(),
              lastMonthReport.expenseTotalAmount(),
              thisMonthReport.budgetAmount(),
              thisMonthReport.expenseTotalAmount());

      monthlyReportResponses.add(monthlyReportResponse);
    }

    return new ExpenseReportResponse(monthlyReportResponses, weeklyReports);
  }
}
