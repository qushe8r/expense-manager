package com.qushe8r.expensemanager.notification.recommendation.dto;

import com.qushe8r.expensemanager.member.entity.Member;
import java.util.List;

public record DailyExpenseRecommendationEvent(
    Member member, List<DailyExpenseRecommendationInformation> informations) {}
