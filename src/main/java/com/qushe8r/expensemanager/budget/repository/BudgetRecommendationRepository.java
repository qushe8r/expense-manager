package com.qushe8r.expensemanager.budget.repository;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.qushe8r.expensemanager.budget.dto.BudgetRecommendationRate;
import com.qushe8r.expensemanager.budget.entity.QBudget;
import com.qushe8r.expensemanager.category.entity.QCategory;
import com.qushe8r.expensemanager.category.entity.QMemberCategory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BudgetRecommendationRepository {

  private static final QBudget BUDGET = QBudget.budget;

  private static final QMemberCategory MEMBER_CATEGORY = QMemberCategory.memberCategory;

  private static final QCategory CATEGORY = QCategory.category;

  private final JPAQueryFactory queryFactory;

  public List<BudgetRecommendationRate> getRecommendation() {

    JPQLQuery<Long> sum = JPAExpressions.select(BUDGET.amount.sum()).from(BUDGET);

    ConstructorExpression<BudgetRecommendationRate> constructor =
        Projections.constructor(
            BudgetRecommendationRate.class,
            CATEGORY.name,
            BUDGET.amount.sum().divide(sum).castToNum(Double.class));

    return queryFactory
        .select(constructor)
        .from(BUDGET)
        .join(MEMBER_CATEGORY)
        .on(BUDGET.memberCategory.id.eq(MEMBER_CATEGORY.id))
        .join(CATEGORY)
        .on(CATEGORY.id.eq(MEMBER_CATEGORY.category.id))
        .groupBy(MEMBER_CATEGORY.category.id)
        .fetch();
  }
}
