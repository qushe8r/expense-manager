package com.qushe8r.expensemanager.member.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class NotificationUrlTest {

  @Test
  void getId() {
    // given
    Member member = new Member(1L);
    Long notificationUrlId = 1L;

    // when
    String discordUrl = "discord";
    NotificationUrl notificationUrl = new NotificationUrl(notificationUrlId, discordUrl, member);

    // then
    Assertions.assertThat(notificationUrl.getId()).isEqualTo(notificationUrlId);
  }

  @Test
  void getDiscordUrl() {
    // given
    Member member = new Member(1L);
    Long notificationUrlId = 1L;

    // when
    String discordUrl = "discord";
    NotificationUrl notificationUrl = new NotificationUrl(notificationUrlId, discordUrl, member);

    // then
    Assertions.assertThat(notificationUrl.getDiscordUrl()).isEqualTo(discordUrl);
  }

  @Test
  void getMember() {
    // given
    Member member = new Member(1L);
    Long notificationUrlId = 1L;

    // when
    String discordUrl = "discord";
    NotificationUrl notificationUrl = new NotificationUrl(notificationUrlId, discordUrl, member);

    // then
    Assertions.assertThat(notificationUrl.getMember()).isEqualTo(member);
  }
}
