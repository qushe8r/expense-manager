package com.qushe8r.expensemanager.budget.controller;

import com.qushe8r.expensemanager.budget.dto.PostBudget;
import com.qushe8r.expensemanager.budget.service.BudgetService;
import com.qushe8r.expensemanager.common.utils.UriCreator;
import com.qushe8r.expensemanager.member.entity.MemberDetails;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/budgets")
@RequiredArgsConstructor
public class BudgetController {

  private static final String BUDGET_DEFAULT_URL = "/budgets";

  private final BudgetService budgetService;

  @PostMapping
  public ResponseEntity<Void> createBudget(
      @AuthenticationPrincipal MemberDetails memberDetails, @RequestBody PostBudget dto) {
    Long budgetId = budgetService.createBudget(memberDetails, dto);
    URI uri = UriCreator.createUri(BUDGET_DEFAULT_URL, budgetId);
    return ResponseEntity.created(uri).build();
  }
}
