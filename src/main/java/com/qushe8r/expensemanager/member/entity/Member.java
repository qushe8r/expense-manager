package com.qushe8r.expensemanager.member.entity;

import com.qushe8r.expensemanager.category.entity.MemberCategory;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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

  @OneToMany(mappedBy = "member")
  private List<MemberCategory> memberCategories = new ArrayList<>();

  public Member(Long id, String email, String password) {
    this.id = id;
    this.email = email;
    this.password = password;
  }

  public Member(Long id) {
    this.id = id;
  }

  public Member(String email, String password) {
    this.email = email;
    this.password = password;
  }
}
