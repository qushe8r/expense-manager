package com.qushe8r.expensemanager.category.dto;

import com.qushe8r.expensemanager.category.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

  public Category toEntity(PostCategory dto) {
    return new Category(dto.name());
  }
}
