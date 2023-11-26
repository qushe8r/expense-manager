package com.qushe8r.expensemanager.notification.evaluation.dto;

import com.qushe8r.expensemanager.member.entity.Member;
import java.util.List;

public record DailyExpenseEvaluationEvent(
    Member member, List<DailyExpenseEvaluationInformation> informations) {}
