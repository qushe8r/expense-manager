package com.qushe8r.expensemanager.budget.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qushe8r.expensemanager.annotation.WithMemberPrincipals;
import com.qushe8r.expensemanager.budget.dto.BudgetResponse;
import com.qushe8r.expensemanager.budget.dto.PatchBudget;
import com.qushe8r.expensemanager.budget.dto.PostBudget;
import com.qushe8r.expensemanager.budget.exception.BudgetExceptionCode;
import com.qushe8r.expensemanager.budget.exception.BudgetNotFoundException;
import com.qushe8r.expensemanager.budget.service.BudgetCreateUseCase;
import com.qushe8r.expensemanager.budget.service.BudgetService;
import com.qushe8r.expensemanager.config.TestSecurityConfig;
import com.qushe8r.expensemanager.matcher.MemberDetailsMatcher;
import com.qushe8r.expensemanager.matcher.PatchBudgetMatcher;
import com.qushe8r.expensemanager.matcher.PostBudgetMatcher;
import com.qushe8r.expensemanager.member.entity.MemberDetails;
import java.time.YearMonth;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(BudgetController.class)
@Import(TestSecurityConfig.class)
@AutoConfigureRestDocs
class BudgetControllerTest {

  private static final String BUDGET_DEFAULT_URL = "/budgets";

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private BudgetCreateUseCase budgetCreateUseCase;

  @MockBean private BudgetService budgetService;

  @DisplayName("createBudget(): 입력값이 유효하면 성공한다")
  @WithMemberPrincipals
  @Test
  void createBudget() throws Exception {
    // given
    Long createdBudgetId = 1L;
    PostBudget postBudget = new PostBudget(100000L, YearMonth.of(2023, 8), 1L);
    String content = objectMapper.writeValueAsString(postBudget);

    BDDMockito.given(
            budgetCreateUseCase.createBudget(
                Mockito.argThat(
                    new MemberDetailsMatcher(new MemberDetails(1L, "test@email.com", "password"))),
                Mockito.argThat(new PostBudgetMatcher(postBudget))))
        .willReturn(createdBudgetId);

    // when
    ResultActions actions =
        mockMvc.perform(
            MockMvcRequestBuilders.post(BUDGET_DEFAULT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content));

    // then
    actions
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(
            MockMvcResultMatchers.header()
                .string(HttpHeaders.LOCATION, BUDGET_DEFAULT_URL + "/" + createdBudgetId))
        .andDo(
            MockMvcRestDocumentation.document(
                "post-budgets",
                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                HeaderDocumentation.responseHeaders(
                    HeaderDocumentation.headerWithName(HttpHeaders.LOCATION)
                        .description("리소스 위치"))));
  }

  @DisplayName("createBudget(): 예산이 null 이라면 실패한다")
  @WithMemberPrincipals
  @Test
  void createBudgetBudgetNull() throws Exception {
    // given
    PostBudget postBudget = new PostBudget(null, YearMonth.of(2023, 8), 1L);
    String content = objectMapper.writeValueAsString(postBudget);

    // when
    ResultActions actions =
        mockMvc.perform(
            MockMvcRequestBuilders.post(BUDGET_DEFAULT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content));

    // then
    actions
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").isEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.status").isEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").isEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors").isNotEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors[0].field").value("amount"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.violationErrors").isEmpty());
  }

  @DisplayName("createBudgetMonthNull(): month가 null 이라면 실패한다")
  @WithMemberPrincipals
  @Test
  void createBudgetMonthNull() throws Exception {
    // given
    PostBudget postBudget = new PostBudget(100000L, null, 1L);
    String content = objectMapper.writeValueAsString(postBudget);

    // when
    ResultActions actions =
        mockMvc.perform(
            MockMvcRequestBuilders.post(BUDGET_DEFAULT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content));

    // then
    actions
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").isEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.status").isEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").isEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors").isNotEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors[0].field").value("month"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.violationErrors").isEmpty());
  }

  @DisplayName("createBudgetCategoryIdNull(): 카테고리 식별자가 null 이라면 실패한다")
  @WithMemberPrincipals
  @Test
  void createBudgetCategoryIdNull() throws Exception {
    // given
    PostBudget postBudget = new PostBudget(100000L, YearMonth.of(2023, 8), null);
    String content = objectMapper.writeValueAsString(postBudget);

    // when
    ResultActions actions =
        mockMvc.perform(
            MockMvcRequestBuilders.post(BUDGET_DEFAULT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content));

    // then
    actions
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").isEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.status").isEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").isEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors").isNotEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors[0].field").value("categoryId"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.violationErrors").isEmpty());
  }

  @DisplayName("modifyBudget(): 입력 값이 유효하면 성공한다.")
  @WithMemberPrincipals
  @Test
  void modifyBudget() throws Exception {
    // given
    Long budgetId = 1L;
    Long modifyAmount = 150000L;
    YearMonth month = YearMonth.of(2023, 8);
    PatchBudget patchBudget = new PatchBudget(modifyAmount);
    String content = objectMapper.writeValueAsString(patchBudget);

    BDDMockito.given(
            budgetService.modifyBudget(
                Mockito.argThat(
                    new MemberDetailsMatcher(new MemberDetails(1L, "test@email.com", "password"))),
                Mockito.eq(budgetId),
                Mockito.argThat(new PatchBudgetMatcher(patchBudget))))
        .willReturn(new BudgetResponse(budgetId, modifyAmount, month));

    // when
    ResultActions actions =
        mockMvc.perform(
            MockMvcRequestBuilders.patch(BUDGET_DEFAULT_URL + "/{budgetId}", budgetId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content));

    // then
    actions
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data").isMap())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.budgetId").isNumber())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.amount").isNumber())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.amount").value(150000L))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.month").isString())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.month").value(month.toString()))
        .andDo(
            MockMvcRestDocumentation.document(
                "patch-budgets",
                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                PayloadDocumentation.responseFields(
                    PayloadDocumentation.fieldWithPath("data")
                        .type(JsonFieldType.OBJECT)
                        .description("데이터"),
                    PayloadDocumentation.fieldWithPath("data.budgetId")
                        .type(JsonFieldType.NUMBER)
                        .description("예산 식별"),
                    PayloadDocumentation.fieldWithPath("data.amount")
                        .type(JsonFieldType.NUMBER)
                        .description("예산 금액"),
                    PayloadDocumentation.fieldWithPath("data.month")
                        .type(JsonFieldType.STRING)
                        .description("예산 시점"))));
  }

  @DisplayName("modifyBudgetBudgetNotFoundException(): 예산을 찾을 수 없습니다.")
  @WithMemberPrincipals
  @Test
  void modifyBudgetBudgetNotFoundException() throws Exception {
    // given
    Long budgetId = 1L;
    Long modifyAmount = 150000L;
    PatchBudget patchBudget = new PatchBudget(modifyAmount);
    String content = objectMapper.writeValueAsString(patchBudget);

    BDDMockito.given(
            budgetService.modifyBudget(
                Mockito.argThat(
                    new MemberDetailsMatcher(new MemberDetails(1L, "test@email.com", "password"))),
                Mockito.eq(budgetId),
                Mockito.argThat(new PatchBudgetMatcher(patchBudget))))
        .willThrow(new BudgetNotFoundException());

    // when
    ResultActions actions =
        mockMvc.perform(
            MockMvcRequestBuilders.patch(BUDGET_DEFAULT_URL + "/{budgetId}", budgetId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content));

    // then
    BudgetExceptionCode errorCode = BudgetExceptionCode.BUDGET_NOT_FOUND;
    actions
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").value(errorCode.getErrorCode()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(errorCode.getStatus()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(errorCode.getMessage()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors").isEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.violationErrors").isEmpty());
  }

  @DisplayName("modifyBudgetAmountNull(): 예산을 찾을 수 없습니다.")
  @WithMemberPrincipals
  @Test
  void modifyBudgetAmountNull() throws Exception {
    // given
    Long budgetId = 1L;
    PatchBudget patchBudget = new PatchBudget(null);
    String content = objectMapper.writeValueAsString(patchBudget);

    // when
    ResultActions actions =
        mockMvc.perform(
            MockMvcRequestBuilders.patch(BUDGET_DEFAULT_URL + "/{budgetId}", budgetId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content));

    // then
    actions
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").isEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.status").isEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").isEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors").isNotEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors[0].field").value("amount"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.violationErrors").isEmpty());
  }

  @DisplayName("deleteBudget(): 정상적으로 삭제되면 noContent 상태코드만 응답한다.")
  @WithMemberPrincipals
  @Test
  void deleteBudget() throws Exception {
    // given
    Long budgetId = 1L;

    BDDMockito.doNothing()
        .when(budgetService)
        .deleteBudget(
            Mockito.argThat(new MemberDetailsMatcher(new MemberDetails(1L, "test@email.com", ""))),
            Mockito.eq(budgetId));

    // when
    ResultActions actions =
        mockMvc.perform(
            RestDocumentationRequestBuilders.delete(BUDGET_DEFAULT_URL + "/{budgetId}", budgetId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

    // then
    actions
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isNoContent())
        .andDo(
            MockMvcRestDocumentation.document(
                "delete-budgets",
                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                RequestDocumentation.pathParameters(
                    RequestDocumentation.parameterWithName("budgetId").description("예산 식별자"))));
  }
}
