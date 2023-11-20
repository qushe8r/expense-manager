package com.qushe8r.expensemanager.expense.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.qushe8r.expensemanager.category.entity.MemberCategory;
import com.qushe8r.expensemanager.category.entity.QCategory;
import com.qushe8r.expensemanager.category.entity.QMemberCategory;
import com.qushe8r.expensemanager.expense.entity.Expense;
import com.qushe8r.expensemanager.expense.entity.QExpense;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ExpenseQueryDslRepository {

  private static final QMemberCategory MEMBER_CATEGORY = QMemberCategory.memberCategory;

  private static final QExpense EXPENSE = QExpense.expense;

  private static final QCategory CATEGORY = QCategory.category;

  private final JPAQueryFactory queryFactory;

  public Optional<Expense> query(Long memberId, Long expenseId) {
    MemberCategory memberCategory =
        queryFactory
            .selectFrom(MEMBER_CATEGORY)
            .join(MEMBER_CATEGORY.expenses, EXPENSE)
            .fetchJoin()
            .join(MEMBER_CATEGORY.category, CATEGORY)
            .fetchJoin()
            .where(MEMBER_CATEGORY.member.id.eq(memberId).and(EXPENSE.id.eq(expenseId)))
            .fetchOne();

    return Optional.ofNullable(memberCategory).map(m -> m.getExpenses().get(0));
  }
}
