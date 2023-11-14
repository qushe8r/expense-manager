package com.qushe8r.expensemanager.budget.mapper;

import com.qushe8r.expensemanager.budget.dto.PostBudget;
import com.qushe8r.expensemanager.budget.entity.Budget;
import com.qushe8r.expensemanager.category.entity.MemberCategory;
import org.springframework.stereotype.Component;

@Component
public class BudgetMapper {

  public Budget toEntity(MemberCategory memberCategory, PostBudget dto) {
    return new Budget(dto.budget(), dto.month(), memberCategory);
  }
}
