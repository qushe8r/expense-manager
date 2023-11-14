package com.qushe8r.expensemanager.budget.service;

import com.qushe8r.expensemanager.budget.dto.PostBudget;
import com.qushe8r.expensemanager.category.entity.Category;
import com.qushe8r.expensemanager.category.entity.MemberCategory;
import com.qushe8r.expensemanager.category.service.CategoryService;
import com.qushe8r.expensemanager.category.service.MemberCategoryService;
import com.qushe8r.expensemanager.member.entity.MemberDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BudgetCreateUseCase {

  private final CategoryService categoryService;

  private final MemberCategoryService memberCategoryService;

  private final BudgetService budgetService;

  @Transactional
  public Long createBudget(MemberDetails memberDetails, PostBudget dto) {
    Category category = categoryService.validateCategoryByIdOrElseThrow(dto.categoryId());

    MemberCategory memberCategory =
        memberCategoryService.findByMemberCategoryOrElseSave(
            memberDetails.getId(), category.getId());

    return budgetService.createBudget(memberCategory, dto);
  }
}
