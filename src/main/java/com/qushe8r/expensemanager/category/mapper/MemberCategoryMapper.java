package com.qushe8r.expensemanager.category.mapper;

import com.qushe8r.expensemanager.category.dto.CategoryTotalsExpenseResponse;
import com.qushe8r.expensemanager.category.dto.GlobalTotalsExpenseResponse;
import com.qushe8r.expensemanager.category.entity.MemberCategory;
import com.qushe8r.expensemanager.expense.entity.Expense;
import com.qushe8r.expensemanager.expense.mapper.ExpenseMapper;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class MemberCategoryMapper {

  public GlobalTotalsExpenseResponse toGlobalTotalsExpenseResponse(
      List<MemberCategory> memberCategories) {
    List<CategoryTotalsExpenseResponse> categoryTotalsExpenseResponses =
        memberCategories.stream().map(this::toCategoryTotalsExpenseResponse).toList();

    Long globalTotals =
        categoryTotalsExpenseResponses.stream()
            .mapToLong(CategoryTotalsExpenseResponse::categoryTotals)
            .sum();

    return new GlobalTotalsExpenseResponse(globalTotals, categoryTotalsExpenseResponses);
  }

  private CategoryTotalsExpenseResponse toCategoryTotalsExpenseResponse(
      MemberCategory memberCategory) {
    return new CategoryTotalsExpenseResponse(
        memberCategory.getCategory().getName(),
        memberCategory.getExpenses().stream().mapToLong(Expense::getAmount).sum(),
        memberCategory.getExpenses().stream()
            .map(ExpenseMapper::toCategorylessExpenseResponse)
            .toList());
  }
}
