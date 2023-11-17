package com.qushe8r.expensemanager.expense.dto;

import java.time.LocalDateTime;

public record ExpenseResponse(
    Long expenseId, Long amount, String memo, LocalDateTime expenseAt, String categoryName) {}
