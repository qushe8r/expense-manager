package com.qushe8r.expensemanager.budget.dto;

import java.time.YearMonth;

public record PostBudget(Long budget, YearMonth month, Long categoryId) {}
