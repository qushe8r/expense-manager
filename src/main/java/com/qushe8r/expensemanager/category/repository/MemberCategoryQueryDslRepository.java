package com.qushe8r.expensemanager.category.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.qushe8r.expensemanager.category.entity.MemberCategory;
import com.qushe8r.expensemanager.category.entity.QCategory;
import com.qushe8r.expensemanager.category.entity.QMemberCategory;
import com.qushe8r.expensemanager.expense.entity.QExpense;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberCategoryQueryDslRepository {

  private static final QMemberCategory MEMBER_CATEGORY = QMemberCategory.memberCategory;

  private static final QCategory CATEGORY = QCategory.category;

  private static final QExpense EXPENSE = QExpense.expense;

  private final JPAQueryFactory queryFactory;

  public List<MemberCategory> query(
      Long memberId, LocalDateTime start, LocalDateTime end, Long categoryId, Long min, Long max) {

    return queryFactory
        .selectFrom(MEMBER_CATEGORY)
        .join(MEMBER_CATEGORY.category, CATEGORY)
        .fetchJoin()
        .join(MEMBER_CATEGORY.expenses, EXPENSE)
        .fetchJoin()
        .where(
            expenseAtGoe(start),
            expenseAtLoe(end),
            categoryNameEq(categoryId),
            expenseAmountGoe(min),
            expenseAmountLoe(max),
            memberIdEq(memberId))
        .orderBy(EXPENSE.expenseAt.desc())
        .fetch();
  }

  private BooleanExpression memberIdEq(Long memberId) {
    return MEMBER_CATEGORY.member.id.eq(memberId);
  }

  private BooleanExpression expenseAmountLoe(Long max) {
    return max == null ? null : EXPENSE.amount.loe(max);
  }

  private BooleanExpression expenseAmountGoe(Long min) {
    return min == null ? null : EXPENSE.amount.goe(min);
  }

  private BooleanExpression categoryNameEq(Long categoryId) {
    return categoryId == null ? null : CATEGORY.id.eq(categoryId);
  }

  private BooleanExpression expenseAtLoe(LocalDateTime end) {
    return EXPENSE.expenseAt.loe(end);
  }

  private BooleanExpression expenseAtGoe(LocalDateTime start) {
    return EXPENSE.expenseAt.goe(start);
  }
}
