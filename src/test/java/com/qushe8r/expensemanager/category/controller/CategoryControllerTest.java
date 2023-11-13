package com.qushe8r.expensemanager.category.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qushe8r.expensemanager.annotation.WebMvcTestWithoutSecurityConfig;
import com.qushe8r.expensemanager.category.dto.PostCategory;
import com.qushe8r.expensemanager.category.service.CategoryService;
import com.qushe8r.expensemanager.macher.PostCategoryMatcher;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTestWithoutSecurityConfig(CategoryController.class)
class CategoryControllerTest {

  private static final String CATEGORY_NAME = "카테고리";

  private static final String CATEGORY_DEFAULT_URL = "/categories";

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private CategoryService categoryService;

  @DisplayName("createCategory(): 입력값이 유효하면 성공한다.")
  @Test
  void createCategory() throws Exception {
    // given
    Long createdCategoryId = 1L;
    PostCategory postCategory = new PostCategory(CATEGORY_NAME);
    String content = objectMapper.writeValueAsString(postCategory);

    BDDMockito.given(
            categoryService.crateCategory(Mockito.argThat(new PostCategoryMatcher(postCategory))))
        .willReturn(createdCategoryId);

    // when
    ResultActions actions =
        mockMvc.perform(
            MockMvcRequestBuilders.post(CATEGORY_DEFAULT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8.name())
                .content(content));

    // then
    actions
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(
            MockMvcResultMatchers.header()
                .string(HttpHeaders.LOCATION, CATEGORY_DEFAULT_URL + "/" + createdCategoryId));
  }

  @DisplayName("createCategoryValidationCategoryName(): category 이름 유효성 검사 실패")
  @ParameterizedTest(name = "categoryName: {0} 유효성 검사")
  @CsvSource({", null은 허용하지 않습니다.", "notKr, 한글만 사용할 수 있습니다."})
  void createCategoryValidationCategoryName(String category, String reason) throws Exception {
    // given
    PostCategory postCategory = new PostCategory(category);
    String content = objectMapper.writeValueAsString(postCategory);

    // when
    ResultActions actions =
        mockMvc.perform(
            MockMvcRequestBuilders.post(CATEGORY_DEFAULT_URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));
    // then
    actions
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").isEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.status").isEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").isEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors").isNotEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors[0].field").value("name"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.violationErrors").isEmpty());
  }
}
