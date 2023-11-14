package com.qushe8r.expensemanager.budget.service;

import com.qushe8r.expensemanager.budget.dto.PostBudget;
import com.qushe8r.expensemanager.member.entity.MemberDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BudgetService {

  public Long createBudget(MemberDetails memberDetails, PostBudget dto) {
    return null;
  }
}
