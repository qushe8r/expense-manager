package com.qushe8r.expensemanager.expense.dto;

public record ExpenseWeeklyReport(
    Long categoryId,
    String categoryName,
    Long twoWeeksAgoExpenseAmount,
    Long oneWeekAgoExpenseAmount) {}
