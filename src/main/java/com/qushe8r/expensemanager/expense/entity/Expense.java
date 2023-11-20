package com.qushe8r.expensemanager.expense.entity;

import com.qushe8r.expensemanager.category.entity.MemberCategory;
import com.qushe8r.expensemanager.expense.dto.PatchExpense;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "expense")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Expense {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "expense_id")
  private Long id;

  private Long amount;

  private String memo;

  private LocalDateTime expenseAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_category_id")
  private MemberCategory memberCategory;

  public Expense(
      Long id, Long amount, String memo, LocalDateTime expenseAt, MemberCategory memberCategory) {
    this.id = id;
    this.amount = amount;
    this.memo = memo;
    this.expenseAt = expenseAt;
    this.memberCategory = memberCategory;
  }

  public Expense(Long amount, String memo, LocalDateTime expenseAt, MemberCategory memberCategory) {
    this.amount = amount;
    this.memo = memo;
    this.expenseAt = expenseAt;
    this.memberCategory = memberCategory;
  }

  public void modify(PatchExpense dto, MemberCategory memberCategory) {
    if (dto.amount() != null) {
      this.amount = dto.amount();
    }
    if (dto.memo() != null) {
      this.memo = dto.memo();
    }
    if (dto.expenseAt() != null) {
      this.expenseAt = dto.expenseAt();
    }
    this.memberCategory = memberCategory;
  }
}
