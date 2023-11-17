package com.qushe8r.expensemanager.category.mapper;

import com.qushe8r.expensemanager.category.dto.CategoryResponse;
import com.qushe8r.expensemanager.category.dto.PostCategory;
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
