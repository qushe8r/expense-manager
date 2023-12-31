package com.qushe8r.expensemanager.category.entity;

import com.qushe8r.expensemanager.budget.entity.Budget;
import com.qushe8r.expensemanager.expense.entity.Expense;
import com.qushe8r.expensemanager.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "member_category")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberCategory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "member_category_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id")
  private Category category;

  @OneToMany(mappedBy = "memberCategory")
  private List<Budget> budgets = new ArrayList<>();

  @OneToMany(mappedBy = "memberCategory")
  private List<Expense> expenses = new ArrayList<>();

  public MemberCategory(Long id, Member member, Category category) {
    this.id = id;
    this.member = member;
    this.category = category;
  }

  public MemberCategory(Member member, Category category) {
    this.member = member;
    this.category = category;
  }

  public MemberCategory(Long id, Member member, Category category, List<Expense> expenses) {
    this.id = id;
    this.member = member;
    this.category = category;
    this.expenses = expenses;
  }
}
