package com.qushe8r.expensemanager.budget.service;

import com.qushe8r.expensemanager.budget.dto.BudgetResponse;
import com.qushe8r.expensemanager.budget.dto.PatchBudget;
import com.qushe8r.expensemanager.budget.dto.PostBudget;
import com.qushe8r.expensemanager.budget.entity.Budget;
import com.qushe8r.expensemanager.budget.exception.BudgetAlreadyExistsException;
import com.qushe8r.expensemanager.budget.exception.BudgetNotFoundException;
import com.qushe8r.expensemanager.budget.mapper.BudgetMapper;
import com.qushe8r.expensemanager.budget.repository.BudgetRepository;
import com.qushe8r.expensemanager.category.entity.MemberCategory;
import com.qushe8r.expensemanager.member.entity.MemberDetails;
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
    validateByMonthIfPresentThrow(memberCategory, dto);
    Budget rowBudget = budgetMapper.toEntity(memberCategory, dto);
    Budget budget = budgetRepository.save(rowBudget);
    return budget.getId();
  }

  @Transactional
  public BudgetResponse modifyBudget(MemberDetails memberDetails, Long budgetId, PatchBudget dto) {
    Budget budget =
        budgetRepository
            .findBudgetByIdAndMemberId(memberDetails.getId(), budgetId)
            .orElseThrow(BudgetNotFoundException::new);
    budget.modify(dto.amount());
    return budgetMapper.toResponse(budget);
  }

  @Transactional
  public void deleteBudget(MemberDetails memberDetails, Long budgetId) {
    budgetRepository
        .findBudgetByIdAndMemberId(memberDetails.getId(), budgetId)
        .ifPresent(budgetRepository::delete);
  }

  private void validateByMonthIfPresentThrow(MemberCategory memberCategory, PostBudget dto) {
    budgetRepository
        .findByMemberIdAndMonth(memberCategory.getId(), dto.month())
        .ifPresent(
            budget -> {
              throw new BudgetAlreadyExistsException();
            });
  }
}
