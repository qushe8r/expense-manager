package com.qushe8r.expensemanager.category.repository;

import com.qushe8r.expensemanager.category.entity.MemberCategory;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberCategoryRepository extends JpaRepository<MemberCategory, Long> {

  // TODO: category가 필요한 것과 필요 없는 것 나눠서 조회하기?
  @Query(
      "SELECT j FROM MemberCategory j "
          + "JOIN FETCH j.category c "
          + "WHERE j.member.id = :memberId "
          + "AND j.category.id = :categoryId")
  Optional<MemberCategory> findByMemberIdAndCategoryId(Long memberId, Long categoryId);

  // TODO: queryDSL로 동적쿼리로 바꾸기
  @Query(
      "SELECT mc FROM MemberCategory mc "
          + "JOIN FETCH mc.category c "
          + "JOIN FETCH mc.expenses e "
          + "WHERE e.expenseAt >= :start "
          + "AND e.expenseAt <= :end "
          + "AND e.amount >= :min "
          + "AND e.amount <= :max "
          + "AND mc.member.id = :memberId "
          + "ORDER BY e.expenseAt DESC ")
  List<MemberCategory> findCategoriesByAmountRangeAndDateRange(
      Long memberId, LocalDateTime start, LocalDateTime end, Long min, Long max);
}
