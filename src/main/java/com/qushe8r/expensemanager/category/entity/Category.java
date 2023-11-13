package com.qushe8r.expensemanager.category.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter(AccessLevel.PRIVATE)
@Entity
@Table(name = "category")
public class Category {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "category_id")
  private Long id;

  @Column(unique = true, updatable = false)
  private String name;

  public Category(Long id, String name) {
    this.id = id;
    this.name = name;
  }

  public Category(String name) {
    this.name = name;
  }
}
