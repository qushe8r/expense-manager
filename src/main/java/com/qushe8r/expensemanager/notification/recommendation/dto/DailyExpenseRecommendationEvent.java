package com.qushe8r.expensemanager.notification.recommendation.dto;

import java.util.List;

public record DailyExpenseRecommendationEvent(
    List<DailyExpenseRecommendationInformation> informations) {}
