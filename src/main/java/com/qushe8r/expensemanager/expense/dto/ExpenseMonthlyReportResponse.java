package com.qushe8r.expensemanager.expense.dto;

public record ExpenseMonthlyReportResponse(
    Long categoryId,
    String categoryName,
    Long lastMonthBudget,
    Long lastMonthExpenseTotals,
    Long thisMonthBudget,
    Long thisMonthExpenseTotals) {}
