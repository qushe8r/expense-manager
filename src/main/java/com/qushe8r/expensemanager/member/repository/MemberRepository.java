package com.qushe8r.expensemanager.member.repository;

import com.qushe8r.expensemanager.member.entity.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member, Long> {

  Optional<Member> findByEmail(String username);

  @Query(
      """
      SELECT m
      FROM Member m
      JOIN NotificationUrl u ON u.member.id = m.id
      WHERE m.recommendationAlarm = true
      """)
  List<Member> findAllConsentMemberWithNotificationUrls();
}
