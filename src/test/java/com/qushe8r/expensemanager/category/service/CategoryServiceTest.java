package com.qushe8r.expensemanager.category.service;

import com.qushe8r.expensemanager.category.dto.CategoryResponse;
import com.qushe8r.expensemanager.category.dto.PostCategory;
import com.qushe8r.expensemanager.category.entity.Category;
import com.qushe8r.expensemanager.category.exception.CategoryAlreadyExistException;
import com.qushe8r.expensemanager.category.mapper.CategoryMapper;
import com.qushe8r.expensemanager.category.repository.CategoryRepository;
import com.qushe8r.expensemanager.matcher.CategoryMatcher;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

  private static final String CATEGORY_NAME_EXAMPLE = "카테고리";

  @Spy private CategoryMapper categoryMapper;

  @Mock private CategoryRepository categoryRepository;

  @InjectMocks private CategoryService categoryService;

  @DisplayName("createCategory(): 카테고리 등록이 완료되면 카테고리 id를 응답한다.")
  @Test
  void createCategory() {
    // given
    Long expectedId = 1L;
    PostCategory postCategory = new PostCategory(CATEGORY_NAME_EXAMPLE);
    Category rowCategory = new Category(CATEGORY_NAME_EXAMPLE);
    Category category = new Category(expectedId, CATEGORY_NAME_EXAMPLE);

    BDDMockito.given(categoryRepository.findByName(CATEGORY_NAME_EXAMPLE))
        .willReturn(Optional.empty());
    BDDMockito.given(categoryRepository.save(Mockito.argThat(new CategoryMatcher(rowCategory))))
        .willReturn(category);

    // when
    Long categoryId = categoryService.crateCategory(postCategory);

    // then
    Assertions.assertThat(categoryId).isEqualTo(expectedId);
    Mockito.verify(categoryRepository, Mockito.times(1)).findByName(CATEGORY_NAME_EXAMPLE);
    Mockito.verify(categoryRepository, Mockito.times(1))
        .save(Mockito.argThat(new CategoryMatcher(rowCategory)));
  }

  @DisplayName("createCategoryCategoryAlreadyExistException(): 이미 존재하는 카테고리명을 입력하면 예외가 발생한다.")
  @Test
  void createCategoryCategoryAlreadyExistException() {
    // given
    Long expectedId = 1L;
    PostCategory postCategory = new PostCategory(CATEGORY_NAME_EXAMPLE);
    Category category = new Category(expectedId, CATEGORY_NAME_EXAMPLE);

    BDDMockito.given(categoryRepository.findByName(CATEGORY_NAME_EXAMPLE))
        .willReturn(Optional.of(category));

    // when & then
    Assertions.assertThatThrownBy(() -> categoryService.crateCategory(postCategory))
        .isInstanceOf(CategoryAlreadyExistException.class);
    Mockito.verify(categoryRepository, Mockito.times(1)).findByName(CATEGORY_NAME_EXAMPLE);
  }

  @DisplayName("getCategories(): 조회에 성공하면 CategoryResponse 리스트를 반환한다.")
  @Test
  void getCategories() {
    // given
    List<Category> categories = List.of(new Category(1L, CATEGORY_NAME_EXAMPLE));
    BDDMockito.given(categoryRepository.findAll()).willReturn(categories);

    // when
    List<CategoryResponse> response = categoryService.getCategories();

    // then
    Assertions.assertThat(response).isNotEmpty();
    Assertions.assertThat(response.get(0))
        .hasFieldOrPropertyWithValue("id", 1L)
        .hasFieldOrPropertyWithValue("name", CATEGORY_NAME_EXAMPLE);
  }
}
