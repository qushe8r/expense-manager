package com.qushe8r.expensemanager.budget.entity;

import com.qushe8r.expensemanager.category.entity.MemberCategory;
import com.qushe8r.expensemanager.common.converter.YearMonthAttributeConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.YearMonth;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "budget")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Budget {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "budget_id")
  private Long id;

  private Long amount;

  @Column(columnDefinition = "date")
  @Convert(converter = YearMonthAttributeConverter.class)
  private YearMonth month;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_category_id")
  private MemberCategory memberCategory;

  public Budget(Long id, Long amount, YearMonth month, MemberCategory memberCategory) {
    this.id = id;
    this.amount = amount;
    this.month = month;
    this.memberCategory = memberCategory;
  }

  public Budget(Long amount, YearMonth month, MemberCategory memberCategory) {
    this.amount = amount;
    this.month = month;
    this.memberCategory = memberCategory;
  }

  public void modify(Long amount) {
    this.amount = amount;
  }
}
