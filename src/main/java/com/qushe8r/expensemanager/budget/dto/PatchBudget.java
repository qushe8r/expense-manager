package com.qushe8r.expensemanager.budget.dto;

import jakarta.validation.constraints.NotNull;

public record PatchBudget(@NotNull Long amount) {}
