package com.qushe8r.expensemanager.category.dto;

import com.qushe8r.expensemanager.category.entity.Category;

public record CategoryResponse(Long id, String name) {

  public CategoryResponse(Category category) {
    this(category.getId(), category.getName());
  }
}
