package com.qushe8r.expensemanager.expense.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qushe8r.expensemanager.config.TestSecurityConfig;
import com.qushe8r.expensemanager.expense.dto.PostExpense;
import com.qushe8r.expensemanager.expense.service.ExpenseService;
import com.qushe8r.expensemanager.matcher.PostExpenseMatcher;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(ExpenseController.class)
@Import(TestSecurityConfig.class)
class ExpenseControllerTest {

  private static final String EXPENSE_DEFAULT_URL = "/expenses";

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private ExpenseService expenseService;

  @DisplayName("createExpense(): 입력값이 유효하면 성공한다.")
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

    BDDMockito.given(
            expenseService.createExpense(Mockito.argThat(new PostExpenseMatcher(postExpense))))
        .willReturn(createdExpenseId);

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
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(
            MockMvcResultMatchers.header()
                .string(HttpHeaders.LOCATION, EXPENSE_DEFAULT_URL + "/" + createdExpenseId));
    Mockito.verify(expenseService, Mockito.times(1))
        .createExpense(Mockito.argThat(new PostExpenseMatcher(postExpense)));
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
    LocalDateTime expenseAt = LocalDateTime.of(2023, 11, 15, 12, 0);;
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
}
