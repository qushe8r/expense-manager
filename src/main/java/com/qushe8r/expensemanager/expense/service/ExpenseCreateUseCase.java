package com.qushe8r.expensemanager.expense.service;

import com.qushe8r.expensemanager.category.entity.Category;
import com.qushe8r.expensemanager.category.entity.MemberCategory;
import com.qushe8r.expensemanager.category.service.CategoryService;
import com.qushe8r.expensemanager.category.service.MemberCategoryService;
import com.qushe8r.expensemanager.expense.dto.PostExpense;
import com.qushe8r.expensemanager.member.entity.MemberDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExpenseCreateUseCase {

  private final CategoryService categoryService;

  private final MemberCategoryService memberCategoryService;

  private final ExpenseService expenseService;

  @Transactional
  public Long createExpense(MemberDetails memberDetails, PostExpense dto) {
    Category category = categoryService.validateCategoryByIdOrElseThrow(dto.categoryId());

    MemberCategory memberCategory =
        memberCategoryService.findByMemberCategoryOrElseSave(
            memberDetails.getId(), category.getId());

    return expenseService.createExpense(memberCategory, dto);
  }
}
