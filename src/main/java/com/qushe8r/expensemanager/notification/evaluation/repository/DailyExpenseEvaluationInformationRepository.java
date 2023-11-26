package com.qushe8r.expensemanager.notification.evaluation.repository;

import com.qushe8r.expensemanager.notification.evaluation.dto.DailyExpenseEvaluationInformation;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DailyExpenseEvaluationInformationRepository {

  private final EntityManager entityManager;

  public List<DailyExpenseEvaluationInformation> query(
      LocalDateTime firstDay,
      LocalDateTime todayStart,
      LocalDateTime now,
      YearMonth month,
      Long memberId) {
    return entityManager
        .createQuery(
            """
            SELECT
            new com.qushe8r.expensemanager.notification.evaluation.dto.
            DailyExpenseEvaluationInformation(
            b.memberCategory.category.name,
            ifnull(t1.previousTotals, 0),
            ifnull(t1.todayTotals, 0),
            b.amount)
            FROM (
            SELECT c.name as categoryName,
            SUM(CASE WHEN(e.expenseAt BETWEEN :firstDay AND :todayStart AND mc.member.id = :memberId) THEN e.amount ELSE 0 END) as previousTotals,
            SUM(CASE WHEN(e.expenseAt BETWEEN :todayStart AND :now AND mc.member.id = :memberId) THEN e.amount ELSE 0 END) as todayTotals,
            mc.id as member_category_id
            FROM MemberCategory mc
            JOIN Category c ON c.id = mc.category.id
            JOIN Expense e ON e.memberCategory.id = mc.id
            WHERE mc.member.id = :memberId
            GROUP BY mc.id
            ) as t1
            RIGHT JOIN Budget b ON t1.member_category_id = b.memberCategory.id
            WHERE b.month = :month
            AND b.memberCategory.member.id = :memberId
            """,
            DailyExpenseEvaluationInformation.class)
        .setParameter("firstDay", firstDay)
        .setParameter("todayStart", todayStart)
        .setParameter("now", now)
        .setParameter("month", month)
        .setParameter("memberId", memberId)
        .getResultList();
  }
}
