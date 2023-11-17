package com.qushe8r.expensemanager.category.service;

import com.qushe8r.expensemanager.category.dto.CategoryResponse;
import com.qushe8r.expensemanager.category.dto.PostCategory;
import com.qushe8r.expensemanager.category.entity.Category;
import com.qushe8r.expensemanager.category.exception.CategoryAlreadyExistException;
import com.qushe8r.expensemanager.category.exception.CategoryNotFoundException;
import com.qushe8r.expensemanager.category.mapper.CategoryMapper;
import com.qushe8r.expensemanager.category.repository.CategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class CategoryService {

  private final CategoryMapper categoryMapper;

  private final CategoryRepository categoryRepository;

  @Transactional
  public Long crateCategory(PostCategory dto) {
    validateCategoryByNameIfPresentThrow(dto.name());
    Category rowCategory = categoryMapper.toEntity(dto);
    Category category = categoryRepository.save(rowCategory);
    return category.getId();
  }

  public List<CategoryResponse> getCategories() {
    List<Category> categories = categoryRepository.findAll();
    return categoryMapper.toResponse(categories);
  }

  private void validateCategoryByNameIfPresentThrow(String name) {
    categoryRepository
        .findByName(name)
        .ifPresent(
            category -> {
              throw new CategoryAlreadyExistException();
            });
  }

  public Category validateCategoryByIdOrElseThrow(Long categoryId) {
    return categoryRepository.findById(categoryId).orElseThrow(CategoryNotFoundException::new);
  }
}
