package com.qushe8r.expensemanager.category.dto;

import java.util.List;

public record GlobalTotalsExpenseResponse(
    Long globalTotals, List<CategoryTotalsExpenseResponse> categories) {}
