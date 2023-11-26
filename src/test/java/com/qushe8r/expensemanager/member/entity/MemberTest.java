package com.qushe8r.expensemanager.member.entity;

import java.util.Collections;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class MemberTest {

  @Test
  void constructor() {
    // given
    Long memberId = 1L;
    String email = "test@email.com";
    String password = "password";

    // when
    Member member = new Member(memberId, email, password, false, false);

    // then
    Assertions.assertThat(member)
        .hasFieldOrPropertyWithValue("id", memberId)
        .hasFieldOrPropertyWithValue("email", email)
        .hasFieldOrPropertyWithValue("password", password);
  }

  @Test
  void constructorNoId() {
    // given
    String email = "test@email.com";
    String password = "password";

    // when
    Member member = new Member(email, password, false, false);

    // then
    Assertions.assertThat(member)
        .hasFieldOrPropertyWithValue("id", null)
        .hasFieldOrPropertyWithValue("email", email)
        .hasFieldOrPropertyWithValue("password", password);
  }

  @Test
  void getter() {
    // given
    Long memberId = 1L;
    String email = "test@email.com";
    String password = "password";

    // when
    Member member = new Member(memberId, email, password, false, false);

    // then
    Assertions.assertThat(member.getId()).isEqualTo(memberId);
    Assertions.assertThat(member.getEmail()).isEqualTo(email);
    Assertions.assertThat(member.getPassword()).isEqualTo(password);
    Assertions.assertThat(member.getMemberCategories()).isEqualTo(Collections.emptyList());
  }
}
