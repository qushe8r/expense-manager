package com.qushe8r.expensemanager.category.repository;

import com.qushe8r.expensemanager.category.entity.MemberCategory;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberCategoryRepository extends JpaRepository<MemberCategory, Long> {

  @Query(
      value =
          "SELECT j FROM MemberCategory j WHERE j.member.id = :memberId AND j.category.id = :categoryId")
  Optional<MemberCategory> findByMemberIdAndCategoryId(Long memberId, Long categoryId);
}
