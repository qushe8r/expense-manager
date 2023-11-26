package com.qushe8r.expensemanager.integration.expense;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qushe8r.expensemanager.config.DataSourceSelector;
import com.qushe8r.expensemanager.expense.dto.PatchExpense;
import com.qushe8r.expensemanager.expense.dto.PostExpense;
import com.qushe8r.expensemanager.security.jwt.JwtProperties;
import com.qushe8r.expensemanager.security.jwt.TokenProvider;
import com.qushe8r.expensemanager.stub.JwtFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ExpenseControllerWriteIntegrationTest {

  private static final String EXPENSE_DEFAULT_URL = "/expenses";

  private static final String EXPENSE_PATH_PARAMETER = "/{expenseId}";

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Autowired private JwtProperties jwtProperties;

  @Autowired private DataSourceSelector dataSourceSelector;

  @BeforeEach
  void setUp() {
    dataSourceSelector.toWrite();
  }

  @DisplayName("createExpense(): 입력값이 유효하면 성공한다.")
  @Test
  void createExpense() throws Exception {
    // given
    String accessToken = JwtFactory.withDefaultValues().generateToken(jwtProperties);

    Long amount = 10000L;
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
                .header(HttpHeaders.AUTHORIZATION, TokenProvider.BEARER + accessToken)
                .content(content));

    // then
    actions
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.header().exists(HttpHeaders.LOCATION));
  }

  @DisplayName("createExpenseCategoryNotFoundException(): 존재하지 않는 카테고리 id를 입력하면 예외가 발생한다.")
  @Test
  void createExpenseCategoryNotFoundException() throws Exception {
    // given
    String accessToken = JwtFactory.withDefaultValues().generateToken(jwtProperties);

    Long amount = 10000L;
    String memo = "돼지국밥";
    LocalDateTime expenseDate = LocalDateTime.of(2023, 11, 15, 12, 0);
    Long categoryId = Long.MAX_VALUE;

    PostExpense postExpense = new PostExpense(amount, memo, expenseDate, categoryId);
    String content = objectMapper.writeValueAsString(postExpense);

    // when
    ResultActions actions =
        mockMvc.perform(
            MockMvcRequestBuilders.post(EXPENSE_DEFAULT_URL)
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

  @DisplayName("modifyExpense(): 입력값이 유효하면 성공한다.")
  @Test
  void modifyExpense() throws Exception {
    // given
    String accessToken = JwtFactory.withDefaultValues().generateToken(jwtProperties);

    Long expenseId = 1L;

    Long amount = 10000L;
    String memo = "돼지국밥";
    LocalDateTime expenseAt = LocalDateTime.of(2023, 11, 15, 12, 0);
    Long categoryId = 1L;

    PatchExpense patchExpense = new PatchExpense(amount, memo, expenseAt, categoryId);
    String content = objectMapper.writeValueAsString(patchExpense);

    // when
    ResultActions actions =
        mockMvc.perform(
            MockMvcRequestBuilders.patch(EXPENSE_DEFAULT_URL + EXPENSE_PATH_PARAMETER, expenseId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, TokenProvider.BEARER + accessToken)
                .content(content));

    // then
    actions
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data").isMap())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.expenseId").isNumber())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.amount").value(amount))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.memo").value(memo))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.data.expenseAt")
                .value(expenseAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))));
  }

  @DisplayName("modifyExpense(): 다른 유저가 접근하면 예외가 발생한다")
  @Test
  void modifyExpenseAnotherUser() throws Exception {
    // given
    String anotherUserAccessToken = JwtFactory.withAnotherUserValues().generateToken(jwtProperties);

    Long expenseId = 1L;

    Long amount = 10000L;
    String memo = "돼지국밥";
    LocalDateTime expenseAt = LocalDateTime.of(2023, 11, 15, 12, 0);
    Long categoryId = 1L;

    PatchExpense patchExpense = new PatchExpense(amount, memo, expenseAt, categoryId);
    String content = objectMapper.writeValueAsString(patchExpense);

    // when
    ResultActions actions =
        mockMvc.perform(
            MockMvcRequestBuilders.patch(EXPENSE_DEFAULT_URL + EXPENSE_PATH_PARAMETER, expenseId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, TokenProvider.BEARER + anotherUserAccessToken)
                .content(content));

    // then
    actions
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").value("EX01"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(404))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("예산을 찾을 수 없습니다."))
        .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors").isEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.violationErrors").isEmpty());
  }

  @DisplayName("modifyExpense(): 입력값이 유효하면 성공한다.")
  @Test
  void modifyExpenseCategoryNotFoundException() throws Exception {
    // given
    String accessToken = JwtFactory.withDefaultValues().generateToken(jwtProperties);

    Long expenseId = 1L;

    Long amount = 10000L;
    String memo = "돼지국밥";
    LocalDateTime expenseAt = LocalDateTime.of(2023, 11, 15, 12, 0);
    Long categoryId = Long.MAX_VALUE;

    PatchExpense patchExpense = new PatchExpense(amount, memo, expenseAt, categoryId);
    String content = objectMapper.writeValueAsString(patchExpense);

    // when
    ResultActions actions =
        mockMvc.perform(
            MockMvcRequestBuilders.patch(EXPENSE_DEFAULT_URL + EXPENSE_PATH_PARAMETER, expenseId)
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

  @DisplayName("modifyExpense(): 입력값이 유효하면 성공한다.")
  @Test
  void modifyExpenseExpenseNotFoundException() throws Exception {
    // given
    String accessToken = JwtFactory.withDefaultValues().generateToken(jwtProperties);

    Long expenseId = Long.MAX_VALUE;

    Long amount = 10000L;
    String memo = "돼지국밥";
    LocalDateTime expenseAt = LocalDateTime.of(2023, 11, 15, 12, 0);
    Long categoryId = 1L;

    PatchExpense patchExpense = new PatchExpense(amount, memo, expenseAt, categoryId);
    String content = objectMapper.writeValueAsString(patchExpense);

    // when
    ResultActions actions =
        mockMvc.perform(
            MockMvcRequestBuilders.patch(EXPENSE_DEFAULT_URL + EXPENSE_PATH_PARAMETER, expenseId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, TokenProvider.BEARER + accessToken)
                .content(content));

    // then
    actions
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").value("EX01"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(404))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("예산을 찾을 수 없습니다."))
        .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors").isEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.violationErrors").isEmpty());
  }

  @DisplayName("deleteExpense(): 입력값이 유효하면 204 NoContent 응답한다.")
  @Test
  void deleteExpense() throws Exception {
    // given
    String accessToken = JwtFactory.withDefaultValues().generateToken(jwtProperties);

    Long expenseId = 1L;

    // when
    ResultActions actions =
        mockMvc.perform(
            MockMvcRequestBuilders.delete(EXPENSE_DEFAULT_URL + EXPENSE_PATH_PARAMETER, expenseId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, TokenProvider.BEARER + accessToken));

    // then
    actions
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isNoContent());
  }

  @DisplayName("deleteExpense(): 입력값이 유효하면 204 NoContent 응답한다.")
  @Test
  void deleteExpenseInvalidUser() throws Exception {
    // given
    String anotherUserAccessToken = JwtFactory.withAnotherUserValues().generateToken(jwtProperties);

    Long expenseId = 1L;

    // when
    ResultActions actions =
        mockMvc.perform(
            MockMvcRequestBuilders.delete(EXPENSE_DEFAULT_URL + EXPENSE_PATH_PARAMETER, expenseId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, TokenProvider.BEARER + anotherUserAccessToken));

    // then
    actions
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isNoContent());
  }
}
