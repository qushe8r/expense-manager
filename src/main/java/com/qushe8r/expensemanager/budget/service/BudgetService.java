package com.qushe8r.expensemanager.budget.service;

import com.qushe8r.expensemanager.budget.dto.PostBudget;
import com.qushe8r.expensemanager.budget.entity.Budget;
import com.qushe8r.expensemanager.budget.exception.BudgetAlreadyExistsException;
import com.qushe8r.expensemanager.budget.mapper.BudgetMapper;
import com.qushe8r.expensemanager.budget.repository.BudgetRepository;
import com.qushe8r.expensemanager.category.entity.MemberCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class BudgetService {

  private final BudgetMapper budgetMapper;

  private final BudgetRepository budgetRepository;

  @Transactional
  public Long createBudget(MemberCategory memberCategory, PostBudget dto) {
    validateByMonthIfPresentThrow(dto);
    Budget rowBudget = budgetMapper.toEntity(memberCategory, dto);
    Budget budget = budgetRepository.save(rowBudget);
    return budget.getId();
  }

  private void validateByMonthIfPresentThrow(PostBudget dto) {
    budgetRepository
        .findByMonth(dto.month())
        .ifPresent(
            budget -> {
              throw new BudgetAlreadyExistsException();
            });
  }
}
