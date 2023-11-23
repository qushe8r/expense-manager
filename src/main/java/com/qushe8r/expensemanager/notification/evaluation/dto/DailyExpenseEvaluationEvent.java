package com.qushe8r.expensemanager.notification.evaluation.dto;

import java.util.List;

public record DailyExpenseEvaluationEvent(List<DailyExpenseEvaluationInformation> informations) {}
