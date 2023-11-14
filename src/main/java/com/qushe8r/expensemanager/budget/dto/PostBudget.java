package com.qushe8r.expensemanager.budget.dto;

import com.qushe8r.expensemanager.category.dto.PostBudgetCategory;
import java.time.YearMonth;

public record PostBudget(Long budget, YearMonth month, PostBudgetCategory category) {}
