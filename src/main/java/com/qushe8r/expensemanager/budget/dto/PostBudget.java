package com.qushe8r.expensemanager.budget.dto;

import jakarta.validation.constraints.NotNull;
import java.time.YearMonth;

public record PostBudget(
    @NotNull Long budget, @NotNull YearMonth month, @NotNull Long categoryId) {}
