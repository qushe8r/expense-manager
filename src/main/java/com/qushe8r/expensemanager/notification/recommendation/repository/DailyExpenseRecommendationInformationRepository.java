package com.qushe8r.expensemanager.notification.recommendation.repository;

import com.qushe8r.expensemanager.notification.recommendation.dto.DailyExpenseRecommendationInformation;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DailyExpenseRecommendationInformationRepository {

  private final EntityManager entityManager;

  public List<DailyExpenseRecommendationInformation> query(
      Long memberId, LocalDateTime start, LocalDateTime end, YearMonth month) {
    return entityManager
        .createQuery(
            """
                SELECT new com.qushe8r.expensemanager.notification.recommendation.dto.
                DailyExpenseRecommendationInformation(t1.categoryName, b.amount, t1.expense)
                FROM (SELECT SUM(e.amount) as expense, mc.category.name as categoryName, mc.id as memberCategoryId
                FROM Expense e JOIN MemberCategory mc ON e.memberCategory.id = mc.id
                WHERE mc.member.id = :memberId
                AND e.expenseAt BETWEEN :start AND :end
                GROUP BY mc.id) as t1
                JOIN Budget b ON t1.memberCategoryId = b.memberCategory.id
                WHERE b.month = :month
                """,
            DailyExpenseRecommendationInformation.class)
        .setParameter("memberId", memberId)
        .setParameter("start", start)
        .setParameter("end", end)
        .setParameter("month", month)
        .getResultList();
  }
}
