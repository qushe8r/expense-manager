package com.qushe8r.expensemanager.member.service;

import com.qushe8r.expensemanager.macher.MemberMatcher;
import com.qushe8r.expensemanager.member.dto.PostMember;
import com.qushe8r.expensemanager.member.entity.Member;
import com.qushe8r.expensemanager.member.exception.MemberAlreadyExistException;
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
}
