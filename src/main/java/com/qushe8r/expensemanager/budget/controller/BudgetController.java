package com.qushe8r.expensemanager.budget.controller;

import com.qushe8r.expensemanager.budget.dto.BudgetRecommendationResponse;
import com.qushe8r.expensemanager.budget.dto.BudgetResponse;
import com.qushe8r.expensemanager.budget.dto.PatchBudget;
import com.qushe8r.expensemanager.budget.dto.PostBudget;
import com.qushe8r.expensemanager.budget.service.BudgetCreateUseCase;
import com.qushe8r.expensemanager.budget.service.BudgetService;
import com.qushe8r.expensemanager.common.dto.SingleResponse;
import com.qushe8r.expensemanager.common.utils.UriCreator;
import com.qushe8r.expensemanager.member.entity.MemberDetails;
import jakarta.validation.constraints.Positive;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/budgets")
@RequiredArgsConstructor
public class BudgetController {

  private static final String BUDGET_DEFAULT_URL = "/budgets";

  private final BudgetCreateUseCase budgetCreateUseCase;

  private final BudgetService budgetService;

  @PostMapping
  public ResponseEntity<Void> createBudget(
      @AuthenticationPrincipal MemberDetails memberDetails,
      @RequestBody @Validated PostBudget dto) {
    Long budgetId = budgetCreateUseCase.createBudget(memberDetails, dto);
    URI uri = UriCreator.createUri(BUDGET_DEFAULT_URL, budgetId);
    return ResponseEntity.created(uri).build();
  }

  @PatchMapping("/{budgetId}")
  public ResponseEntity<SingleResponse<BudgetResponse>> modifyBudget(
      @AuthenticationPrincipal MemberDetails memberDetails,
      @PathVariable Long budgetId,
      @RequestBody @Validated PatchBudget dto) {
    BudgetResponse response = budgetService.modifyBudget(memberDetails, budgetId, dto);
    return ResponseEntity.ok(new SingleResponse<>(response));
  }

  @DeleteMapping("/{budgetId}")
  public ResponseEntity<Void> deleteBudget(
      @AuthenticationPrincipal MemberDetails memberDetails, @PathVariable Long budgetId) {
    budgetService.deleteBudget(memberDetails, budgetId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/recommendation")
  public ResponseEntity<SingleResponse<List<BudgetRecommendationResponse>>> getRecommendation(
      @RequestParam @Positive Long amount) {
    List<BudgetRecommendationResponse> response = budgetService.getRecommendation(amount);
    return ResponseEntity.ok(new SingleResponse<>(response));
  }
}
