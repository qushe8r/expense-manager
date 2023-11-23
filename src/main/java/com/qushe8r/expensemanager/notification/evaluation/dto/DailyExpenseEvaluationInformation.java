package com.qushe8r.expensemanager.notification.evaluation.dto;

public record DailyExpenseEvaluationInformation(
    String categoryName,
    Long previousDayTotalExpenses,
    Long todayTotalExpenses,
    Long budgetAmount) {}
