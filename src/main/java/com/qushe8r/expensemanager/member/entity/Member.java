package com.qushe8r.expensemanager.member.entity;

import com.qushe8r.expensemanager.category.entity.MemberCategory;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "member-id")
  private Long id;

  @Column(unique = true, updatable = false, nullable = false)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private Boolean evaluationAlarm;

  @Column(nullable = false)
  private Boolean recommendationAlarm;

  @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
  private NotificationUrl notificationUrl;

  @OneToMany(mappedBy = "member")
  private List<MemberCategory> memberCategories = new ArrayList<>();

  public Member(
      Long id,
      String email,
      String password,
      Boolean evaluationAlarm,
      Boolean recommendationAlarm) {
    this.id = id;
    this.email = email;
    this.password = password;
    this.evaluationAlarm = evaluationAlarm;
    this.recommendationAlarm = recommendationAlarm;
  }

  public Member(
      Long id,
      String email,
      String password,
      Boolean evaluationAlarm,
      Boolean recommendationAlarm,
      NotificationUrl notificationUrl) {
    this.id = id;
    this.email = email;
    this.password = password;
    this.evaluationAlarm = evaluationAlarm;
    this.recommendationAlarm = recommendationAlarm;
    this.notificationUrl = notificationUrl;
  }

  public Member(Long id) {
    this.id = id;
  }

  public Member(
      String email, String password, Boolean evaluationAlarm, Boolean recommendationAlarm) {
    this.email = email;
    this.password = password;
    this.evaluationAlarm = evaluationAlarm;
    this.recommendationAlarm = recommendationAlarm;
  }

  public void modifyPassword(String newPassword) {
    this.password = newPassword;
  }
}
