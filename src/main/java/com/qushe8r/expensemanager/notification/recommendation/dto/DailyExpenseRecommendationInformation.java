package com.qushe8r.expensemanager.notification.recommendation.dto;

public record DailyExpenseRecommendationInformation(
    String categoryName, Long budgeAmount, Long expenseTotals) {}
