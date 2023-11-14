package com.qushe8r.expensemanager.category.entity;

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
import lombok.Setter;

@Getter
@Setter(AccessLevel.PRIVATE)
@Entity
@Table(name = "category")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "category_id")
  private Long id;

  @Column(unique = true, updatable = false)
  private String name;

  @OneToMany(mappedBy = "category")
  private List<MemberCategory> memberCategory = new ArrayList<>();

  public Category(Long id, String name) {
    this.id = id;
    this.name = name;
  }

  public Category(Long id) {
    this.id = id;
  }

  public Category(String name) {
    this.name = name;
  }
}
