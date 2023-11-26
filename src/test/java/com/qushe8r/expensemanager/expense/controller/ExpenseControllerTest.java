package com.qushe8r.expensemanager.expense.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qushe8r.expensemanager.annotation.WithMemberPrincipals;
import com.qushe8r.expensemanager.config.TestSecurityConfig;
import com.qushe8r.expensemanager.expense.dto.ExpenseMonthlyReportResponse;
import com.qushe8r.expensemanager.expense.dto.ExpenseReportResponse;
import com.qushe8r.expensemanager.expense.dto.ExpenseResponse;
import com.qushe8r.expensemanager.expense.dto.ExpenseWeeklyReport;
import com.qushe8r.expensemanager.expense.dto.PatchExpense;
import com.qushe8r.expensemanager.expense.dto.PostExpense;
import com.qushe8r.expensemanager.expense.service.ExpenseCreateUseCase;
import com.qushe8r.expensemanager.expense.service.ExpenseService;
import com.qushe8r.expensemanager.expense.service.ExpenseUpdateUseCase;
import com.qushe8r.expensemanager.matcher.MemberDetailsMatcher;
import com.qushe8r.expensemanager.matcher.PatchExpenseMatcher;
import com.qushe8r.expensemanager.matcher.PostExpenseMatcher;
import com.qushe8r.expensemanager.member.entity.MemberDetails;
import java.time.LocalDateTime;
import java.util.List;
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

@WebMvcTest(ExpenseController.class)
@Import(TestSecurityConfig.class)
@AutoConfigureRestDocs
class ExpenseControllerTest {

  private static final String EXPENSE_DEFAULT_URL = "/expenses";

  private static final String EXPENSE_PATH_PARAMETER = "/{expenseId}";

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private ExpenseCreateUseCase expenseCreateUseCase;

  @MockBean private ExpenseUpdateUseCase expenseUpdateUseCase;

  @MockBean private ExpenseService expenseService;

  @DisplayName("createExpense(): 입력값이 유효하면 성공한다.")
  @WithMemberPrincipals
  @Test
  void createExpense() throws Exception {
    // given
    Long createdExpenseId = 1L;

    Long amount = 10000L;
    String memo = "돼지국밥";
    LocalDateTime expenseDate = LocalDateTime.of(2023, 11, 15, 12, 0);
    Long categoryId = 1L;

    PostExpense postExpense = new PostExpense(amount, memo, expenseDate, categoryId);
    String content = objectMapper.writeValueAsString(postExpense);

    MemberDetails memberDetails = new MemberDetails(1L, "test@email.com", "");

    BDDMockito.given(
            expenseCreateUseCase.createExpense(
                Mockito.argThat(new MemberDetailsMatcher(memberDetails)),
                Mockito.argThat(new PostExpenseMatcher(postExpense))))
        .willReturn(createdExpenseId);

    // when
    ResultActions actions =
        mockMvc.perform(
            RestDocumentationRequestBuilders.post(EXPENSE_DEFAULT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content));

    // then
    actions
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(
            MockMvcResultMatchers.header()
                .string(HttpHeaders.LOCATION, EXPENSE_DEFAULT_URL + "/" + createdExpenseId))
        .andDo(
            MockMvcRestDocumentation.document(
                "post-expenses",
                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                PayloadDocumentation.requestFields(
                    PayloadDocumentation.fieldWithPath("amount").description("지출 금액"),
                    PayloadDocumentation.fieldWithPath("memo").description("지출 메모"),
                    PayloadDocumentation.fieldWithPath("expenseAt").description("지출 시점"),
                    PayloadDocumentation.fieldWithPath("categoryId").description("카테고리 식별자")),
                HeaderDocumentation.responseHeaders(
                    HeaderDocumentation.headerWithName(HttpHeaders.LOCATION)
                        .description("리소스 위치"))));

    Mockito.verify(expenseCreateUseCase, Mockito.times(1))
        .createExpense(
            Mockito.argThat(new MemberDetailsMatcher(memberDetails)),
            Mockito.argThat(new PostExpenseMatcher(postExpense)));
  }

  @DisplayName("createExpenseAmountNull(): amount가 null이면 400 BadRequest 응답한다.")
  @Test
  void createExpenseAmountNull() throws Exception {
    // given
    Long amount = null;
    String memo = "돼지국밥";
    LocalDateTime expenseDate = LocalDateTime.of(2023, 11, 15, 12, 0);
    Long categoryId = 1L;

    PostExpense postExpense = new PostExpense(amount, memo, expenseDate, categoryId);
    String content = objectMapper.writeValueAsString(postExpense);

    // when
    ResultActions actions =
        mockMvc.perform(
            MockMvcRequestBuilders.post(EXPENSE_DEFAULT_URL)
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

  @DisplayName("createExpenseExpenseAtNull(): expenseAt가 null이면 400 BadRequest 응답한다.")
  @Test
  void createExpenseExpenseAtNull() throws Exception {
    // given
    Long amount = 10000L;
    String memo = "돼지국밥";
    LocalDateTime expenseAt = null;
    Long categoryId = 1L;

    PostExpense postExpense = new PostExpense(amount, memo, expenseAt, categoryId);
    String content = objectMapper.writeValueAsString(postExpense);

    // when
    ResultActions actions =
        mockMvc.perform(
            MockMvcRequestBuilders.post(EXPENSE_DEFAULT_URL)
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
        .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors[0].field").value("expenseAt"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.violationErrors").isEmpty());
  }

  @DisplayName("createExpenseCategoryIdNull(): categoryId가 null이면 400 BadRequest 응답한다.")
  @Test
  void createExpenseCategoryIdNull() throws Exception {
    // given
    Long amount = 10000L;
    String memo = "돼지국밥";
    LocalDateTime expenseAt = LocalDateTime.of(2023, 11, 15, 12, 0);
    Long categoryId = null;

    PostExpense postExpense = new PostExpense(amount, memo, expenseAt, categoryId);
    String content = objectMapper.writeValueAsString(postExpense);

    // when
    ResultActions actions =
        mockMvc.perform(
            MockMvcRequestBuilders.post(EXPENSE_DEFAULT_URL)
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

  @DisplayName("modifyExpense(): 입력값이 유효하면 성공한다.")
  @WithMemberPrincipals
  @Test
  void modifyExpense() throws Exception {
    // given
    Long expenseId = 1L;

    Long amount = 10000L;
    String memo = "돼지국밥";
    LocalDateTime expenseAt = LocalDateTime.of(2023, 11, 15, 12, 0);
    Long categoryId = 1L;
    String categoryName = "카테고리";

    PatchExpense patchExpense = new PatchExpense(amount, memo, expenseAt, categoryId);
    String content = objectMapper.writeValueAsString(patchExpense);

    MemberDetails memberDetails = new MemberDetails(1L, "test@email.com", "");

    BDDMockito.given(
            expenseUpdateUseCase.modifyExpense(
                Mockito.argThat(new MemberDetailsMatcher(memberDetails)),
                Mockito.eq(expenseId),
                Mockito.argThat(new PatchExpenseMatcher(patchExpense))))
        .willReturn(new ExpenseResponse(expenseId, amount, memo, expenseAt, categoryName));

    // when
    ResultActions actions =
        mockMvc.perform(
            RestDocumentationRequestBuilders.patch(
                    EXPENSE_DEFAULT_URL + EXPENSE_PATH_PARAMETER, expenseId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content));

    // then
    actions
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andDo(
            MockMvcRestDocumentation.document(
                "patch-expenses",
                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                PayloadDocumentation.requestFields(
                    PayloadDocumentation.fieldWithPath("amount").description("지출 금액").optional(),
                    PayloadDocumentation.fieldWithPath("memo").description("지출 메모").optional(),
                    PayloadDocumentation.fieldWithPath("expenseAt").description("지출 시점").optional(),
                    PayloadDocumentation.fieldWithPath("categoryId")
                        .description("카테고리 식별자")
                        .optional()),
                RequestDocumentation.pathParameters(
                    RequestDocumentation.parameterWithName("expenseId").description("지출 식별자")),
                PayloadDocumentation.responseFields(
                    PayloadDocumentation.fieldWithPath("data")
                        .type(JsonFieldType.OBJECT)
                        .description("데이터"),
                    PayloadDocumentation.fieldWithPath("data.expenseId")
                        .type(JsonFieldType.NUMBER)
                        .description("지출 식별자"),
                    PayloadDocumentation.fieldWithPath("data.amount")
                        .type(JsonFieldType.NUMBER)
                        .description("지출 금액"),
                    PayloadDocumentation.fieldWithPath("data.memo")
                        .type(JsonFieldType.STRING)
                        .description("지출 메모"),
                    PayloadDocumentation.fieldWithPath("data.expenseAt")
                        .type(JsonFieldType.STRING)
                        .description("예산 시점"),
                    PayloadDocumentation.fieldWithPath("data.categoryName")
                        .type(JsonFieldType.STRING)
                        .description("지출 카테고리 이름"))));

    Mockito.verify(expenseUpdateUseCase, Mockito.times(1))
        .modifyExpense(
            Mockito.argThat(new MemberDetailsMatcher(memberDetails)),
            Mockito.eq(expenseId),
            Mockito.argThat(new PatchExpenseMatcher(patchExpense)));
  }

  @DisplayName("deleteExpense(): 입력값이 유효하면 204 NoContent 응답한다.")
  @WithMemberPrincipals
  @Test
  void deleteExpense() throws Exception {
    // given
    Long expenseId = 1L;
    MemberDetails memberDetails = new MemberDetails(1L, "test@email.com", "");

    BDDMockito.doNothing().when(expenseService).deleteExpense(memberDetails.getId(), expenseId);

    // when
    ResultActions actions =
        mockMvc.perform(
            RestDocumentationRequestBuilders.delete(
                    EXPENSE_DEFAULT_URL + EXPENSE_PATH_PARAMETER, expenseId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

    // then
    actions
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isNoContent())
        .andDo(
            MockMvcRestDocumentation.document(
                "delete-expenses",
                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                RequestDocumentation.pathParameters(
                    RequestDocumentation.parameterWithName("expenseId").description("지출 식별자"))));
    Mockito.verify(expenseService, Mockito.times(1))
        .deleteExpense(memberDetails.getId(), expenseId);
  }

  @DisplayName("deleteExpenseExpenseIdNotPositive(): expenseId가 양의 정수가 아니면 유효성 검증 실패한다.")
  @WithMemberPrincipals
  @Test
  void deleteExpenseExpenseIdNotPositive() throws Exception {
    // given
    Long expenseId = 0L;

    BDDMockito.doNothing().when(expenseService).deleteExpense(Mockito.anyLong(), Mockito.anyLong());

    // when
    ResultActions actions =
        mockMvc.perform(
            MockMvcRequestBuilders.delete(EXPENSE_DEFAULT_URL + EXPENSE_PATH_PARAMETER, expenseId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

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
            MockMvcResultMatchers.jsonPath("$.violationErrors[0].rejectedValue")
                .value(String.valueOf(expenseId)))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.violationErrors[0].propertyPath")
                .value("deleteExpense.expenseId"));
    Mockito.verify(expenseService, Mockito.times(0))
        .deleteExpense(Mockito.anyLong(), Mockito.anyLong());
  }

  @WithMemberPrincipals
  @Test
  void getReport() throws Exception {
    // given
    MemberDetails memberDetails = new MemberDetails(1L, "test@email.com", "");

    ExpenseMonthlyReportResponse monthlyReportResponse =
        new ExpenseMonthlyReportResponse(1L, "categoryName", 100000L, 35000L, 100000L, 25000L);
    List<ExpenseMonthlyReportResponse> monthlyReports = List.of(monthlyReportResponse);

    ExpenseWeeklyReport expenseWeeklyReport =
        new ExpenseWeeklyReport(1L, "categoryName", 25000L, 10000L);
    List<ExpenseWeeklyReport> weeklyReports = List.of(expenseWeeklyReport);

    ExpenseReportResponse expenseReportResponse =
        new ExpenseReportResponse(monthlyReports, weeklyReports);

    BDDMockito.given(
            expenseService.getReport(Mockito.argThat(new MemberDetailsMatcher(memberDetails))))
        .willReturn(expenseReportResponse);

    // when
    ResultActions actions =
        mockMvc.perform(
            MockMvcRequestBuilders.get(EXPENSE_DEFAULT_URL + "/report")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

    // then
    actions
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data").isMap())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.monthlyReports").isArray())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.monthlyReports[0].categoryId").isNumber())
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.data.monthlyReports[0].categoryName").isString())
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.data.monthlyReports[0].lastMonthBudget").isNumber())
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.data.monthlyReports[0].lastMonthExpenseTotals")
                .isNumber())
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.data.monthlyReports[0].thisMonthBudget").isNumber())
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.data.monthlyReports[0].thisMonthExpenseTotals")
                .isNumber())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.weeklyReports").isArray())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.weeklyReports[0].categoryId").isNumber())
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.data.weeklyReports[0].categoryName").isString())
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.data.weeklyReports[0].twoWeeksAgoExpenseAmount")
                .isNumber())
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.data.weeklyReports[0].oneWeekAgoExpenseAmount")
                .isNumber());

    Mockito.verify(expenseService, Mockito.times(1))
        .getReport(Mockito.argThat(new MemberDetailsMatcher(memberDetails)));
  }
}
