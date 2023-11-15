package com.qushe8r.expensemanager.expense.service;

import com.qushe8r.expensemanager.category.entity.MemberCategory;
import com.qushe8r.expensemanager.expense.dto.PostExpense;
import com.qushe8r.expensemanager.expense.mapper.ExpenseMapper;
import com.qushe8r.expensemanager.expense.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExpenseService {

  private final ExpenseMapper expenseMapper;

  private final ExpenseRepository expenseRepository;

  public Long createExpense(MemberCategory memberCategory, PostExpense dto) {
    return null;
  }
}
