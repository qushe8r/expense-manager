package com.qushe8r.expensemanager.expense.dto;

import java.time.LocalDateTime;

public record CategorylessExpenseResponse(
    Long expenseId, Long amount, String memo, LocalDateTime expenseAt) {}
