package com.qushe8r.expensemanager.expense.dto;

import java.util.List;

public record ExpenseReportResponse(
    List<ExpenseMonthlyReportResponse> monthlyReports, List<ExpenseWeeklyReport> weeklyReports) {}
