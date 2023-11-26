package com.qushe8r.expensemanager.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "notification_url")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationUrl {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "notification_url_id")
  private Long id;

  @Column(name = "discord_url")
  private String discordUrl;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  public NotificationUrl(Long id, String discordUrl, Member member) {
    this.id = id;
    this.discordUrl = discordUrl;
    this.member = member;
  }
}
