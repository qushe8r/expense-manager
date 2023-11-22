package com.qushe8r.expensemanager.expense.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record PostExpense(
    @NotNull Long amount,
    String memo,
    @NotNull LocalDateTime expenseAt,
    @NotNull Long categoryId) {}
