package com.qushe8r.expensemanager.member.mapper;

import com.qushe8r.expensemanager.member.dto.PostMember;
import com.qushe8r.expensemanager.member.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class MemberMapper {

  public Member toEntity(PostMember dto, String encodedPassword) {
    return new Member(dto.email(), encodedPassword);
  }
}
