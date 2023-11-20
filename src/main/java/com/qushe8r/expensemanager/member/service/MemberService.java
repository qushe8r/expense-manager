package com.qushe8r.expensemanager.member.service;

import com.qushe8r.expensemanager.member.dto.PostMember;
import com.qushe8r.expensemanager.member.entity.Member;
import com.qushe8r.expensemanager.member.exception.MemberAlreadyExistException;
import com.qushe8r.expensemanager.member.exception.MemberNotFoundException;
import com.qushe8r.expensemanager.member.mapper.MemberMapper;
import com.qushe8r.expensemanager.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class MemberService {

  private final MemberMapper memberMapper;

  private final PasswordEncoder passwordEncoder;

  private final MemberRepository memberRepository;

  @Transactional
  public Long createMember(PostMember dto) {
    validateMemberExistByEmailIfPresentThrow(dto.email());
    String encodedPassword = passwordEncoder.encode(dto.password());
    Member rowMember = memberMapper.toEntity(dto, encodedPassword);
    Member saved = memberRepository.save(rowMember);
    return saved.getId();
  }

  private void validateMemberExistByEmailIfPresentThrow(String email) {
    memberRepository
        .findByEmail(email)
        .ifPresent(
            member -> {
              throw new MemberAlreadyExistException();
            });
  }

  public Member validateMemberExistByEmailOrElseThrow(String email) {
    return memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);
  }
}
