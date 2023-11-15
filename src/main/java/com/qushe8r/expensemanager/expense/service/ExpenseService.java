package com.qushe8r.expensemanager.expense.service;

import com.qushe8r.expensemanager.category.entity.MemberCategory;
import com.qushe8r.expensemanager.expense.dto.PostExpense;
import com.qushe8r.expensemanager.expense.entity.Expense;
import com.qushe8r.expensemanager.expense.mapper.ExpenseMapper;
import com.qushe8r.expensemanager.expense.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExpenseService {

  private final ExpenseMapper expenseMapper;

  private final ExpenseRepository expenseRepository;

  @Transactional
  public Long createExpense(MemberCategory memberCategory, PostExpense dto) {
    Expense rowBudget = expenseMapper.toEntity(memberCategory, dto);
    Expense budget = expenseRepository.save(rowBudget);
    return budget.getId();
  }
}
