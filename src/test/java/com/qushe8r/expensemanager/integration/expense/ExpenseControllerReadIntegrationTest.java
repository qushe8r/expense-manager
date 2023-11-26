package com.qushe8r.expensemanager.integration.expense;

import com.qushe8r.expensemanager.annotation.WithMemberPrincipals;
import com.qushe8r.expensemanager.config.DataSourceSelector;
import com.qushe8r.expensemanager.security.jwt.JwtProperties;
import com.qushe8r.expensemanager.security.jwt.TokenProvider;
import com.qushe8r.expensemanager.stub.JwtFactory;
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
class ExpenseControllerReadIntegrationTest {

  private static final String EXPENSE_DEFAULT_URL = "/expenses";

  private static final String EXPENSE_PATH_PARAMETER = "/{expenseId}";

  @Autowired private MockMvc mockMvc;

  @Autowired private JwtProperties jwtProperties;

  @Autowired private DataSourceSelector dataSourceSelector;

  @BeforeEach
  void setUp() {
    dataSourceSelector.toRead();
  }

  @DisplayName("modifyExpense(): 입력값이 유효하면 성공한다.")
  @Test
  void modifyExpense() throws Exception {
    // given
    String accessToken = JwtFactory.withDefaultValues().generateToken(jwtProperties);
    Long expenseId = 1L;

    // when
    ResultActions actions =
        mockMvc.perform(
            MockMvcRequestBuilders.get(EXPENSE_DEFAULT_URL + EXPENSE_PATH_PARAMETER, expenseId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, TokenProvider.BEARER + accessToken));

    // then
    actions
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data").isMap())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.expenseId").isNumber())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.amount").isNumber())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.memo").isString())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.expenseAt").isString())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.categoryName").isString());
  }

  @WithMemberPrincipals
  @Test
  void getReport() throws Exception {
    // given
    String accessToken = JwtFactory.withDefaultValues().generateToken(jwtProperties);

    // when
    ResultActions actions =
        mockMvc.perform(
            MockMvcRequestBuilders.get(EXPENSE_DEFAULT_URL + "/report")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, TokenProvider.BEARER + accessToken));

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
  }
}
