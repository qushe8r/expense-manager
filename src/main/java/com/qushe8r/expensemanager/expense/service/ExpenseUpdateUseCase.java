package com.qushe8r.expensemanager.expense.service;

import com.qushe8r.expensemanager.category.entity.MemberCategory;
import com.qushe8r.expensemanager.category.service.MemberCategoryService;
import com.qushe8r.expensemanager.expense.dto.ExpenseResponse;
import com.qushe8r.expensemanager.expense.dto.PatchExpense;
import com.qushe8r.expensemanager.member.entity.MemberDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExpenseUpdateUseCase {

  private final MemberCategoryService memberCategoryService;

  private final ExpenseService expenseService;

  @Transactional
  public ExpenseResponse modifyExpense(
      MemberDetails memberDetails, Long expenseId, PatchExpense dto) {
    MemberCategory memberCategory =
        memberCategoryService.findByMemberCategoryOrElseSave(
            memberDetails.getId(), dto.categoryId());
    return expenseService.modifyExpense(memberCategory, expenseId, dto);
  }
}
