package com.qushe8r.expensemanager.category.repository;

import com.qushe8r.expensemanager.category.entity.MemberCategory;
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
}
