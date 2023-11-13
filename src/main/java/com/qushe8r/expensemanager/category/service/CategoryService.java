package com.qushe8r.expensemanager.category.service;

import com.qushe8r.expensemanager.category.dto.CategoryMapper;
import com.qushe8r.expensemanager.category.dto.PostCategory;
import com.qushe8r.expensemanager.category.entity.Category;
import com.qushe8r.expensemanager.category.exception.CategoryAlreadyExistException;
import com.qushe8r.expensemanager.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class CategoryService {

  private final CategoryMapper categoryMapper;

  private final CategoryRepository categoryRepository;

  public Long crateCategory(PostCategory dto) {
    validateCategoryExistByName(dto.name());
    Category rowCategory = categoryMapper.toEntity(dto);
    Category category = categoryRepository.save(rowCategory);
    return category.getId();
  }

  private void validateCategoryExistByName(String name) {
    categoryRepository
        .findByName(name)
        .ifPresent(
            category -> {
              throw new CategoryAlreadyExistException();
            });
  }
}
