package com.qushe8r.expensemanager.category.controller;

import com.qushe8r.expensemanager.category.dto.CategoryResponse;
import com.qushe8r.expensemanager.category.dto.GlobalTotalsExpenseResponse;
import com.qushe8r.expensemanager.category.dto.PostCategory;
import com.qushe8r.expensemanager.category.service.CategoryService;
import com.qushe8r.expensemanager.category.service.MemberCategoryService;
import com.qushe8r.expensemanager.common.dto.SingleResponse;
import com.qushe8r.expensemanager.common.utils.UriCreator;
import com.qushe8r.expensemanager.member.entity.MemberDetails;
import jakarta.validation.constraints.Positive;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

  private static final String CATEGORY_DEFAULT_URL = "/categories";

  private final CategoryService categoryService;

  private final MemberCategoryService memberCategoryService;

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

  @GetMapping("/expenses")
  public ResponseEntity<SingleResponse<GlobalTotalsExpenseResponse>> getCategorizedExpense(
      @AuthenticationPrincipal MemberDetails memberDetails,
      @RequestParam @DateTimeFormat(iso = ISO.DATE_TIME) LocalDate start,
      @RequestParam @DateTimeFormat(iso = ISO.DATE_TIME) LocalDate end,
      @RequestParam(required = false) Long categoryId,
      @RequestParam(required = false) @Positive Long min,
      @RequestParam(required = false) @Positive Long max) {
    GlobalTotalsExpenseResponse response =
        memberCategoryService.getCategorizedExpense(memberDetails, start, end, categoryId, min, max);
    return ResponseEntity.ok(new SingleResponse<>(response));
  }
}
