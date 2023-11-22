package com.qushe8r.expensemanager.budget.dto;

import java.time.YearMonth;

public record BudgetResponse(Long budgetId, Long amount, YearMonth month) {}
