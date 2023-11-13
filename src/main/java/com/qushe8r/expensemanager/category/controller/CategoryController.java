package com.qushe8r.expensemanager.category.controller;

import com.qushe8r.expensemanager.category.dto.CategoryResponse;
import com.qushe8r.expensemanager.category.dto.PostCategory;
import com.qushe8r.expensemanager.category.service.CategoryService;
import com.qushe8r.expensemanager.common.dto.SingleResponse;
import com.qushe8r.expensemanager.common.utils.UriCreator;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

  private static final String CATEGORY_DEFAULT_URL = "/categories";

  private final CategoryService categoryService;

  @PostMapping
  public ResponseEntity<Void> createCategory(@RequestBody @Validated PostCategory dto) {
    Long categoryId = categoryService.crateCategory(dto);
    URI uri = UriCreator.createUri(CATEGORY_DEFAULT_URL, categoryId);
    return ResponseEntity.created(uri).build();
  }

  @GetMapping
  public ResponseEntity<SingleResponse<List<CategoryResponse>>> getCategories() {
    List<CategoryResponse> response = categoryService.getCategories();
    return ResponseEntity.ok(new SingleResponse<>(response));
  }
}
