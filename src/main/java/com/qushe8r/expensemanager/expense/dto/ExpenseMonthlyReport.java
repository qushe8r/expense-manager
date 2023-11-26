package com.qushe8r.expensemanager.expense.dto;

public record ExpenseMonthlyReport(
    Long categoryId, String categoryName, Long budgetAmount, Long expenseTotalAmount) {}
