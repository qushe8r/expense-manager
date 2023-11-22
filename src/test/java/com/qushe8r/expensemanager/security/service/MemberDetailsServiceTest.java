package com.qushe8r.expensemanager.security.service;

import com.qushe8r.expensemanager.member.entity.Member;
import com.qushe8r.expensemanager.member.entity.MemberDetails;
import com.qushe8r.expensemanager.member.repository.MemberRepository;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
class MemberDetailsServiceTest {

  @Mock private MemberRepository memberRepository;

  @InjectMocks private MemberDetailsService memberDetailsService;

  @Test
  void loadUserByUsername() {
    // given
    String email = "test@email.com";
    Member member = new Member(1L, email, "encodedPassword");
    BDDMockito.given(memberRepository.findByEmail(member.getEmail()))
        .willReturn(Optional.of(member));

    // when
    UserDetails result = memberDetailsService.loadUserByUsername(email);

    // then
    Assertions.assertThat(result)
        .isInstanceOf(MemberDetails.class)
        .hasFieldOrPropertyWithValue("id", member.getId())
        .hasFieldOrPropertyWithValue("email", member.getEmail())
        .hasFieldOrPropertyWithValue("password", member.getPassword());
  }

  @Test
  void loadUserByUsernameUsernameNotFoundException() {
    // given
    String email = "test@email.com";
    Member member = new Member(1L, email, "encodedPassword");
    BDDMockito.given(memberRepository.findByEmail(member.getEmail())).willReturn(Optional.empty());

    // when & then
    Assertions.assertThatThrownBy(() -> memberDetailsService.loadUserByUsername(email))
        .isInstanceOf(UsernameNotFoundException.class)
        .hasMessage("회원을 찾을 수 없습니다.");
  }
}
