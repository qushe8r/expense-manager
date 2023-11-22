package com.qushe8r.expensemanager.category.dto;

import com.qushe8r.expensemanager.expense.dto.CategorylessExpenseResponse;
import java.util.List;

public record CategoryTotalsExpenseResponse(
    String categoryName, Long categoryTotals, List<CategorylessExpenseResponse> expenses) {}
