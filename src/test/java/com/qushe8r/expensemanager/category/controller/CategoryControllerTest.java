package com.qushe8r.expensemanager.category.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qushe8r.expensemanager.annotation.WithMemberPrincipals;
import com.qushe8r.expensemanager.category.dto.CategoryResponse;
import com.qushe8r.expensemanager.category.dto.CategoryTotalsExpenseResponse;
import com.qushe8r.expensemanager.category.dto.GlobalTotalsExpenseResponse;
import com.qushe8r.expensemanager.category.dto.PostCategory;
import com.qushe8r.expensemanager.category.service.CategoryService;
import com.qushe8r.expensemanager.category.service.MemberCategoryService;
import com.qushe8r.expensemanager.config.TestSecurityConfig;
import com.qushe8r.expensemanager.expense.dto.CategorylessExpenseResponse;
import com.qushe8r.expensemanager.matcher.MemberDetailsMatcher;
import com.qushe8r.expensemanager.matcher.PostCategoryMatcher;
import com.qushe8r.expensemanager.member.entity.MemberDetails;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.headers.HeaderDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@WebMvcTest(CategoryController.class)
@Import(TestSecurityConfig.class)
@AutoConfigureRestDocs
class CategoryControllerTest {

  private static final String CATEGORY_NAME = "카테고리";

  private static final String CATEGORY_DEFAULT_URL = "/categories";

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private CategoryService categoryService;

  @MockBean private MemberCategoryService memberCategoryService;

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
                .string(HttpHeaders.LOCATION, CATEGORY_DEFAULT_URL + "/" + createdCategoryId))
        .andDo(
            MockMvcRestDocumentation.document(
                "post-categories",
                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                HeaderDocumentation.responseHeaders(
                    HeaderDocumentation.headerWithName(HttpHeaders.LOCATION)
                        .description("리소스 위치"))));
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

  @DisplayName("getCategories(): 조회 성공시 카테고리 목록을 반환한다.")
  @Test
  void getCategories() throws Exception {
    // given
    List<CategoryResponse> response = List.of(new CategoryResponse(1L, CATEGORY_NAME));
    BDDMockito.given(categoryService.getCategories()).willReturn(response);
    // when
    ResultActions actions =
        mockMvc.perform(
            MockMvcRequestBuilders.get(CATEGORY_DEFAULT_URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON));

    // then
    actions
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").isNumber())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name").isString())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[?(@.id == 1)].name").value(CATEGORY_NAME))
        .andDo(
            MockMvcRestDocumentation.document(
                "get-categories",
                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                PayloadDocumentation.responseFields(
                    PayloadDocumentation.fieldWithPath("data")
                        .type(JsonFieldType.ARRAY)
                        .description("데이터 목록"),
                    PayloadDocumentation.fieldWithPath("data[].id")
                        .type(JsonFieldType.NUMBER)
                        .description("카테고리 식별자"),
                    PayloadDocumentation.fieldWithPath("data[].name")
                        .type(JsonFieldType.STRING)
                        .description("카테고리 이름"))));
  }

  @DisplayName("getCategorizedExpense(): 조회 성공시 GlobalTotalsExpenseResponse(dto)를 반환한다.")
  @WithMemberPrincipals
  @Test
  void getCategorizedExpense() throws Exception {
    // given

    MemberDetails memberDetails = new MemberDetails(1L, "test@email.com", "");
    LocalDate start = LocalDate.of(2023, 11, 1);
    LocalDate end = LocalDate.of(2023, 11, 30);
    Long categoryId = 1L;
    Long min = 10000L;
    Long max = 50000L;

    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("start", start.toString());
    params.add("end", end.toString());
    params.add("categoryId", String.valueOf(categoryId));
    params.add("min", String.valueOf(min));
    params.add("max", String.valueOf(max));

    GlobalTotalsExpenseResponse globalTotalsExpenseResponse = getGlobalTotalsExpenseResponse();

    BDDMockito.given(
            memberCategoryService.getCategorizedExpense(
                Mockito.argThat(new MemberDetailsMatcher(memberDetails)),
                Mockito.eq(start),
                Mockito.eq(end),
                Mockito.eq(categoryId),
                Mockito.eq(min),
                Mockito.eq(max)))
        .willReturn(globalTotalsExpenseResponse);

    // when
    ResultActions actions =
        mockMvc.perform(
            MockMvcRequestBuilders.get(CATEGORY_DEFAULT_URL + "/expenses")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .params(params));

    // then
    actions
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data").isMap())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.globalTotals").isNumber())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.categories").isArray())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.categories[0].categoryName").isString())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.categories[0].categoryTotals").isNumber())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.categories[0].expenses").isArray())
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.data.categories[0].expenses[0].expenseId").isNumber())
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.data.categories[0].expenses[0].amount").isNumber())
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.data.categories[0].expenses[0].memo").isString())
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.data.categories[0].expenses[0].expenseAt").isString())
        .andDo(
            MockMvcRestDocumentation.document(
                "get-categorized-expenses",
                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                PayloadDocumentation.responseFields(
                    PayloadDocumentation.fieldWithPath("data")
                        .type(JsonFieldType.OBJECT)
                        .description("데이터"),
                    PayloadDocumentation.fieldWithPath("data.globalTotals")
                        .type(JsonFieldType.NUMBER)
                        .description("모든 카테고리 총합"),
                    PayloadDocumentation.fieldWithPath("data.categories")
                        .type(JsonFieldType.ARRAY)
                        .description("카테고리 목록"),
                    PayloadDocumentation.fieldWithPath("data.categories[].categoryName")
                        .type(JsonFieldType.STRING)
                        .description("카테고리 이름"),
                    PayloadDocumentation.fieldWithPath("data.categories[].categoryTotals")
                        .type(JsonFieldType.NUMBER)
                        .description("카테고리 총합"),
                    PayloadDocumentation.fieldWithPath("data.categories[].expenses")
                        .type(JsonFieldType.ARRAY)
                        .description("카테고리 별 지출 목록"),
                    PayloadDocumentation.fieldWithPath("data.categories[].expenses[].expenseId")
                        .type(JsonFieldType.NUMBER)
                        .description("지출 식별자"),
                    PayloadDocumentation.fieldWithPath("data.categories[].expenses[].amount")
                        .type(JsonFieldType.NUMBER)
                        .description("지출 금액"),
                    PayloadDocumentation.fieldWithPath("data.categories[].expenses[].memo")
                        .type(JsonFieldType.STRING)
                        .description("지출 메모"),
                    PayloadDocumentation.fieldWithPath("data.categories[].expenses[].expenseAt")
                        .type(JsonFieldType.STRING)
                        .description("지출 시기"))));
  }

  @DisplayName("getCategorizedExpenseMinNotPositive(): min이 양의 정수가 아니면 에러가 발생한다.")
  @WithMemberPrincipals
  @Test
  void getCategorizedExpenseMinNotPositive() throws Exception {
    // given
    LocalDate start = LocalDate.of(2023, 11, 1);
    LocalDate end = LocalDate.of(2023, 11, 30);
    Long min = -1L;
    Long max = 50000L;

    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("start", start.toString());
    params.add("end", end.toString());
    params.add("min", String.valueOf(min));
    params.add("max", String.valueOf(max));

    // when
    ResultActions actions =
        mockMvc.perform(
            MockMvcRequestBuilders.get(CATEGORY_DEFAULT_URL + "/expenses")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .params(params));

    // then
    actions
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").isEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.status").isEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").isEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors").isEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.violationErrors").isArray())
        .andExpect(MockMvcResultMatchers.jsonPath("$.violationErrors[0].propertyPath").isString())
        .andExpect(MockMvcResultMatchers.jsonPath("$.violationErrors[0].rejectedValue").isString())
        .andExpect(MockMvcResultMatchers.jsonPath("$.violationErrors[0].reason").isString())
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.violationErrors[0].propertyPath")
                .value("getCategorizedExpense.min"));
  }

  @DisplayName("getCategorizedExpenseMaxNotPositive(): max가 양의 정수가 아니면 에러가 발생한다.")
  @WithMemberPrincipals
  @Test
  void getCategorizedExpenseMaxNotPositive() throws Exception {
    // given
    LocalDate start = LocalDate.of(2023, 11, 1);
    LocalDate end = LocalDate.of(2023, 11, 30);
    Long min = 10000L;
    Long max = 0L;

    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("start", start.toString());
    params.add("end", end.toString());
    params.add("min", String.valueOf(min));
    params.add("max", String.valueOf(max));

    // when
    ResultActions actions =
        mockMvc.perform(
            MockMvcRequestBuilders.get(CATEGORY_DEFAULT_URL + "/expenses")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .params(params));

    // then
    actions
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").isEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.status").isEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").isEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors").isEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.violationErrors").isArray())
        .andExpect(MockMvcResultMatchers.jsonPath("$.violationErrors[0].propertyPath").isString())
        .andExpect(MockMvcResultMatchers.jsonPath("$.violationErrors[0].rejectedValue").isString())
        .andExpect(MockMvcResultMatchers.jsonPath("$.violationErrors[0].reason").isString())
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.violationErrors[0].propertyPath")
                .value("getCategorizedExpense.max"));
  }

  private GlobalTotalsExpenseResponse getGlobalTotalsExpenseResponse() {
    CategorylessExpenseResponse categorylessExpenseResponse =
        new CategorylessExpenseResponse(1L, 30000L, "선물", LocalDateTime.of(2023, 11, 11, 12, 0));

    List<CategorylessExpenseResponse> categorylessExpenseResponses =
        List.of(categorylessExpenseResponse);

    CategoryTotalsExpenseResponse categoryTotalsExpenseResponse =
        new CategoryTotalsExpenseResponse(CATEGORY_NAME, 30000L, categorylessExpenseResponses);

    List<CategoryTotalsExpenseResponse> categoryTotalsExpenseResponses =
        List.of(categoryTotalsExpenseResponse);

    return new GlobalTotalsExpenseResponse(100000L, categoryTotalsExpenseResponses);
  }
}
