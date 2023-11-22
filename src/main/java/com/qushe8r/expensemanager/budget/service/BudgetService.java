package com.qushe8r.expensemanager.budget.service;

import com.qushe8r.expensemanager.budget.dto.BudgetRecommendationRate;
import com.qushe8r.expensemanager.budget.dto.BudgetRecommendationResponse;
import com.qushe8r.expensemanager.budget.dto.BudgetResponse;
import com.qushe8r.expensemanager.budget.dto.PatchBudget;
import com.qushe8r.expensemanager.budget.dto.PostBudget;
import com.qushe8r.expensemanager.budget.entity.Budget;
import com.qushe8r.expensemanager.budget.exception.BudgetAlreadyExistsException;
import com.qushe8r.expensemanager.budget.exception.BudgetNotFoundException;
import com.qushe8r.expensemanager.budget.mapper.BudgetMapper;
import com.qushe8r.expensemanager.budget.repository.BudgetRecommendationRepository;
import com.qushe8r.expensemanager.budget.repository.BudgetRepository;
import com.qushe8r.expensemanager.category.entity.MemberCategory;
import com.qushe8r.expensemanager.member.entity.MemberDetails;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class BudgetService {

  private final BudgetMapper budgetMapper;

  private final BudgetRepository budgetRepository;

  private final BudgetRecommendationRepository budgetRecommendationRepository;

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

  public List<BudgetRecommendationResponse> getRecommendation(Long amount) {
    List<BudgetRecommendationRate> categorizedRecommendationRates =
        budgetRecommendationRepository.getRecommendation();

    long etcTotal =
        categorizedRecommendationRates.stream()
            .filter(recommendation -> !isOverTenPercentOrNotEtcCategory(recommendation))
            .mapToLong(rate -> (long) (rate.rate() * amount))
            .sum();

    List<BudgetRecommendationResponse> responses =
        categorizedRecommendationRates.stream()
            .filter(this::isOverTenPercentOrNotEtcCategory)
            .map(
                recommendation ->
                    new BudgetRecommendationResponse(
                        recommendation.categoryName(), (long) (recommendation.rate() * amount)))
            .collect(Collectors.toCollection(ArrayList::new));

    responses.add(new BudgetRecommendationResponse("기타", etcTotal));

    return responses;
  }

  private boolean isOverTenPercentOrNotEtcCategory(BudgetRecommendationRate recommendation) {
    return recommendation.rate() >= 0.1 && !recommendation.categoryName().equals("기타");
  }
}
