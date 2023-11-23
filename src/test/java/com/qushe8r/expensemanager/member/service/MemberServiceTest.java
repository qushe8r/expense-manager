package com.qushe8r.expensemanager.member.service;

import com.qushe8r.expensemanager.matcher.MemberMatcher;
import com.qushe8r.expensemanager.member.dto.PatchPassword;
import com.qushe8r.expensemanager.member.dto.PostMember;
import com.qushe8r.expensemanager.member.entity.Member;
import com.qushe8r.expensemanager.member.entity.MemberDetails;
import com.qushe8r.expensemanager.member.exception.MemberAlreadyExistException;
import com.qushe8r.expensemanager.member.exception.MemberNotFoundException;
import com.qushe8r.expensemanager.member.mapper.MemberMapper;
import com.qushe8r.expensemanager.member.repository.MemberRepository;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

  @Spy private MemberMapper memberMapper;

  @Mock private PasswordEncoder passwordEncoder;

  @Mock private MemberRepository memberRepository;

  @InjectMocks private MemberService memberService;

  @DisplayName("createMember(): 회원가입이 완료되면 가입된 회원 id를 응답한다.")
  @Test
  void createMember() {
    // given
    Long expectedId = 1L;
    String email = "test@email.com";
    String password = "password";
    String encodedPassword = "encodedPassword";

    PostMember postMember = new PostMember(email, password);

    BDDMockito.given(passwordEncoder.encode(password)).willReturn(encodedPassword);

    BDDMockito.given(memberRepository.findByEmail(email)).willReturn(Optional.empty());

    BDDMockito.given(
            memberRepository.save(
                Mockito.argThat(new MemberMatcher(new Member(email, encodedPassword)))))
        .willReturn(new Member(expectedId, email, encodedPassword));

    // when
    Long memberId = memberService.createMember(postMember);

    // then
    Assertions.assertThat(memberId).isEqualTo(expectedId);
    Mockito.verify(passwordEncoder, Mockito.times(1)).encode(password);
    Mockito.verify(memberRepository, Mockito.times(1)).findByEmail(email);
    Mockito.verify(memberRepository, Mockito.times(1))
        .save(Mockito.argThat(new MemberMatcher(new Member(email, encodedPassword))));
  }

  @DisplayName(
      "createMemberMemberAlreadyExistException(): 이미 가입된 email은 MemberAlreadyExistException이 발생한다.")
  @Test
  void createMemberMemberAlreadyExistException() {
    // given
    String email = "test@email.com";
    String password = "password";
    String encodedPassword = "encodedPassword";

    PostMember postMember = new PostMember(email, password);

    BDDMockito.given(memberRepository.findByEmail(email))
        .willReturn(Optional.of(new Member(1L, email, encodedPassword)));

    // when & then
    Assertions.assertThatThrownBy(() -> memberService.createMember(postMember))
        .isInstanceOf(MemberAlreadyExistException.class);
    Mockito.verify(memberRepository, Mockito.times(1)).findByEmail(email);
  }

  @DisplayName("validateMemberExistByEmailOrElseThrow(): 멤버가 존재하면 응답해준다")
  @Test
  void validateMemberExistByEmailOrElseThrow() {
    // given
    Long memberId = 1L;
    String email = "test@email.com";
    String password = "password";

    Member member = new Member(memberId, email, password);

    BDDMockito.given(memberRepository.findByEmail(email)).willReturn(Optional.of(member));

    // when
    Member result = memberService.validateMemberExistByEmailOrElseThrow(email);

    // then
    Assertions.assertThat(result)
        .hasFieldOrPropertyWithValue("id", memberId)
        .hasFieldOrPropertyWithValue("email", email)
        .hasFieldOrPropertyWithValue("password", password);
  }

  @DisplayName(
      "validateMemberExistByEmailOrElseThrowMemberNotFoundException(): 멤버가 존재하지 않으면 예외를 던진다")
  @Test
  void validateMemberExistByEmailOrElseThrowMemberNotFoundException() {
    // given
    String email = "test@email.com";

    BDDMockito.given(memberRepository.findByEmail(email)).willReturn(Optional.empty());

    // when & then
    Assertions.assertThatThrownBy(() -> memberService.validateMemberExistByEmailOrElseThrow(email))
        .isInstanceOf(MemberNotFoundException.class);
  }

  @DisplayName("modifyPassword(): 정상적으로 비밀번호가 변경되면 아무런 응답도 없다.")
  @Test
  void modifyPassword() {
    // given
    Long memberId = 1L;
    String newPassword = "newPassword";
    String encodedNewPassword = "encodedNewPassword";
    MemberDetails memberDetails = new MemberDetails(memberId, "test@email.com", "");
    PatchPassword dto = new PatchPassword(newPassword);
    Member member = new Member(memberId, "test@email.com", "oldPassword");

    BDDMockito.given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
    BDDMockito.given(passwordEncoder.encode(newPassword)).willReturn(encodedNewPassword);

    // when
    memberService.modifyPassword(memberDetails, dto);

    // then
    Assertions.assertThat(member.getPassword()).isEqualTo(encodedNewPassword);
  }

  @DisplayName("modifyPasswordMemberNotFoundException(): 회원 조회에 실패하면 에외가 발생한다")
  @Test
  void modifyPasswordMemberNotFoundException() {
    // given
    Long memberId = 1L;
    String newPassword = "newPassword";
    MemberDetails memberDetails = new MemberDetails(memberId, "test@email.com", "");
    PatchPassword dto = new PatchPassword(newPassword);

    BDDMockito.given(memberRepository.findById(memberId)).willReturn(Optional.empty());

    // when & then
    Assertions.assertThatThrownBy(() -> memberService.modifyPassword(memberDetails, dto))
        .isInstanceOf(MemberNotFoundException.class);
  }
}
