package com.qushe8r.expensemanager.expense.mapper;

import com.qushe8r.expensemanager.category.entity.MemberCategory;
import com.qushe8r.expensemanager.expense.dto.PostExpense;
import com.qushe8r.expensemanager.expense.entity.Expense;
import org.springframework.stereotype.Component;

@Component
public class ExpenseMapper {

  public Expense toEntity(MemberCategory memberCategory, PostExpense dto) {
    return new Expense(dto.amount(), dto.memo(), dto.expenseAt(), memberCategory);
  }
}
