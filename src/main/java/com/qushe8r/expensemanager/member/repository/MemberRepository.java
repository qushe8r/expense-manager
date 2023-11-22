package com.qushe8r.expensemanager.member.repository;

import com.qushe8r.expensemanager.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

  Optional<Member> findByEmail(String username);
}
