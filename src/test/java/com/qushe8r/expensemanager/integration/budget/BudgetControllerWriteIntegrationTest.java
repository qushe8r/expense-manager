package com.qushe8r.expensemanager.integration.budget;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qushe8r.expensemanager.annotation.WithMemberPrincipals;
import com.qushe8r.expensemanager.budget.dto.BudgetResponse;
import com.qushe8r.expensemanager.budget.dto.PatchBudget;
import com.qushe8r.expensemanager.budget.dto.PostBudget;
import com.qushe8r.expensemanager.budget.exception.BudgetExceptionCode;
import com.qushe8r.expensemanager.budget.exception.BudgetNotFoundException;
import com.qushe8r.expensemanager.config.DataSourceSelector;
import com.qushe8r.expensemanager.matcher.MemberDetailsMatcher;
import com.qushe8r.expensemanager.matcher.PatchBudgetMatcher;
import com.qushe8r.expensemanager.member.entity.MemberDetails;
import com.qushe8r.expensemanager.security.jwt.JwtProperties;
import com.qushe8r.expensemanager.security.jwt.TokenProvider;
import com.qushe8r.expensemanager.stub.JwtFactory;
import java.time.YearMonth;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class BudgetControllerWriteIntegrationTest {

  private static final String BUDGET_DEFAULT_URL = "/budgets";

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Autowired private JwtProperties jwtProperties;

  @Autowired private DataSourceSelector dataSourceSelector;

  @BeforeEach
  void setUp() {
    dataSourceSelector.toWrite();
  }

  @DisplayName("createBudget(): 입력값이 유효하면 성공한다")
  @Test
  void createBudget() throws Exception {
    // given
    String accessToken = JwtFactory.withDefaultValues().generateToken(jwtProperties);

    PostBudget postBudget = new PostBudget(100000L, YearMonth.of(2023, 10), 1L);
    String content = objectMapper.writeValueAsString(postBudget);

    // when
    ResultActions actions =
        mockMvc.perform(
            MockMvcRequestBuilders.post(BUDGET_DEFAULT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, TokenProvider.BEARER + accessToken)
                .content(content));

    // then
    actions
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.header().exists(HttpHeaders.LOCATION));
  }

  @DisplayName("createBudgetCategoryNotFoundException(): 존재하지 않는 카테고리 id를 입력하면 예외가 발생한다")
  @Test
  void createBudgetCategoryNotFoundException() throws Exception {
    // given
    String accessToken = JwtFactory.withDefaultValues().generateToken(jwtProperties);

    PostBudget postBudget = new PostBudget(100000L, YearMonth.of(2023, 10), Long.MAX_VALUE);
    String content = objectMapper.writeValueAsString(postBudget);

    // when
    ResultActions actions =
        mockMvc.perform(
            MockMvcRequestBuilders.post(BUDGET_DEFAULT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, TokenProvider.BEARER + accessToken)
                .content(content));

    // then
    actions
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").value("MX01"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(404))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("존재하지 않는 카테고리입니다."))
        .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors").isEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.violationErrors").isEmpty());
  }

  @DisplayName("createBudgetBudgetAlreadyExistException(): 이미 예산이 존재하는 month를 입력하면 예외가 발생한다")
  @Test
  void createBudgetBudgetAlreadyExistException() throws Exception {
    // given
    String accessToken = JwtFactory.withDefaultValues().generateToken(jwtProperties);

    PostBudget postBudget = new PostBudget(100000L, YearMonth.of(2023, 11), 1L);
    String content = objectMapper.writeValueAsString(postBudget);

    // when
    ResultActions actions =
        mockMvc.perform(
            MockMvcRequestBuilders.post(BUDGET_DEFAULT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, TokenProvider.BEARER + accessToken)
                .content(content));

    // then
    actions
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isConflict())
        .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").value("BX02"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(409))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("이미 예산이 존재합니다."))
        .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors").isEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.violationErrors").isEmpty());
  }

  @DisplayName("modifyBudget(): 입력 값이 유효하면 성공한다.")
  @Test
  void modifyBudget() throws Exception {
    // given
    String accessToken = JwtFactory.withDefaultValues().generateToken(jwtProperties);

    Long budgetId = 1L;
    Long modifyAmount = 150000L;
    YearMonth month = YearMonth.of(2023, 11);
    PatchBudget patchBudget = new PatchBudget(modifyAmount);
    String content = objectMapper.writeValueAsString(patchBudget);

    // when
    ResultActions actions =
        mockMvc.perform(
            MockMvcRequestBuilders.patch(BUDGET_DEFAULT_URL + "/{budgetId}", budgetId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, TokenProvider.BEARER + accessToken)
                .content(content));

    // then
    actions
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data").isMap())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.budgetId").isNumber())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.budgetId").value(budgetId))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.amount").isNumber())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.amount").value(150000L))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.month").isString())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.month").value(month.toString()));
  }

  @DisplayName("modifyBudgetBudgetNotFoundException(): 예산을 찾을 수 없습니다.")
  @WithMemberPrincipals
  @Test
  void modifyBudgetBudgetNotFoundException() throws Exception {
    // given
    Long budgetId = Long.MAX_VALUE;
    Long modifyAmount = 150000L;
    PatchBudget patchBudget = new PatchBudget(modifyAmount);
    String content = objectMapper.writeValueAsString(patchBudget);

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

  @DisplayName("deleteBudget(): 정상적으로 삭제되면 noContent 상태코드만 응답한다.")
  @Test
  void deleteBudget() throws Exception {
    // given
    String accessToken = JwtFactory.withDefaultValues().generateToken(jwtProperties);

    Long budgetId = 1L;

    // when
    ResultActions actions =
        mockMvc.perform(
            MockMvcRequestBuilders.delete(BUDGET_DEFAULT_URL + "/{budgetId}", budgetId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, TokenProvider.BEARER + accessToken));

    // then
    actions
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isNoContent());
  }

  @DisplayName("deleteBudget(): 존재하지 않는 id를 입력해도 noContent 상태코드만 응답한다.")
  @Test
  void deleteBudgetNotExistId() throws Exception {
    // given
    String accessToken = JwtFactory.withDefaultValues().generateToken(jwtProperties);

    Long budgetId = Long.MAX_VALUE;

    // when
    ResultActions actions =
        mockMvc.perform(
            MockMvcRequestBuilders.delete(BUDGET_DEFAULT_URL + "/{budgetId}", budgetId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, TokenProvider.BEARER + accessToken));

    // then
    actions
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isNoContent());
  }
}
