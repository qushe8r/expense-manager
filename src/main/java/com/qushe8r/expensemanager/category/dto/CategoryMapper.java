package com.qushe8r.expensemanager.category.dto;

import com.qushe8r.expensemanager.category.entity.Category;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

  public Category toEntity(PostCategory dto) {
    return new Category(dto.name());
  }

  public List<CategoryResponse> toResponse(List<Category> categories) {
    return categories.stream().map(CategoryResponse::new).toList();
  }
}
