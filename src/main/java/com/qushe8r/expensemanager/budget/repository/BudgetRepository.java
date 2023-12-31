package com.qushe8r.expensemanager.budget.repository;

import com.qushe8r.expensemanager.budget.entity.Budget;
import java.time.YearMonth;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BudgetRepository extends JpaRepository<Budget, Long> {

  @Query(
      "SELECT b "
          + "FROM Budget b "
          + "WHERE b.memberCategory.id = :memberCategoryId "
          + "AND b.month = :month")
  Optional<Budget> findByMemberIdAndMonth(Long memberCategoryId, YearMonth month);

  @Query(
      "SELECT b "
          + "FROM Budget b "
          + "WHERE b.memberCategory.member.id = :memberId "
          + "AND b.id = :budgetId")
  Optional<Budget> findBudgetByIdAndMemberId(Long memberId, Long budgetId);
}
