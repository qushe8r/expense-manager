package com.qushe8r.expensemanager.expense.dto;

import java.time.LocalDateTime;

public record PatchExpense(Long amount, String memo, LocalDateTime expenseAt, Long categoryId) {}
