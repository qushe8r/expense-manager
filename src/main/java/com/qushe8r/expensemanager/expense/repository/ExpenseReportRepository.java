package com.qushe8r.expensemanager.expense.repository;

import com.qushe8r.expensemanager.expense.dto.ExpenseMonthlyReport;
import com.qushe8r.expensemanager.expense.dto.ExpenseWeeklyReport;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ExpenseReportRepository {

  private final EntityManager entityManager;

  public List<ExpenseMonthlyReport> reportMonthlyExpenses(
      Long memberId, LocalDateTime start, LocalDateTime end, YearMonth month) {
    return entityManager
        .createQuery(
            """
                SELECT new com.qushe8r.expensemanager.expense.dto.
                ExpenseMonthlyReport(b.memberCategory.category.id, b.memberCategory.category.name, b.amount, ifnull(t1.expenseTotals, 0))
                FROM (SELECT sum(se.amount) as expenseTotals, smc.id as MemberCategoryId
                FROM Expense se JOIN MemberCategory smc ON se.memberCategory.id = smc.id
                WHERE smc.member.id = :memberId
                AND se.expenseAt BETWEEN :start AND :end
                GROUP BY smc.id) as t1
                RIGHT JOIN Budget b ON t1.MemberCategoryId = b.memberCategory.id
                WHERE b.month = :month AND b.memberCategory.member.id = :memberId
                """,
            ExpenseMonthlyReport.class)
        .setParameter("memberId", memberId)
        .setParameter("start", start)
        .setParameter("end", end)
        .setParameter("month", month)
        .getResultList();
  }

  public List<ExpenseWeeklyReport> reportWeeklyExpenses(
      Long memberId, LocalDateTime twoWeeksAgo, LocalDateTime oneWeekAgo, LocalDateTime now) {
    return entityManager
        .createQuery(
            """
        SELECT new com.qushe8r.expensemanager.expense.dto.ExpenseWeeklyReport(
        e.memberCategory.category.id,
        e.memberCategory.category.name,
        sum(CASE WHEN e.expenseAt BETWEEN :twoWeeksAgo AND :oneWeekAgo THEN e.amount ELSE 0 END),
        sum(CASE WHEN e.expenseAt BETWEEN :oneWeekAgo AND :now THEN e.amount ELSE 0 END))
        FROM MemberCategory mc
        JOIN Expense e ON mc.id = e.memberCategory.id
        WHERE e.expenseAt BETWEEN :twoWeeksAgo AND :now
        AND mc.member.id = :memberId
        GROUP BY mc.id
        """,
            ExpenseWeeklyReport.class)
        .setParameter("memberId", memberId)
        .setParameter("twoWeeksAgo", twoWeeksAgo)
        .setParameter("oneWeekAgo", oneWeekAgo)
        .setParameter("now", now)
        .getResultList();
  }
}
