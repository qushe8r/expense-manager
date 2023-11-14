package com.qushe8r.expensemanager.budget.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qushe8r.expensemanager.annotation.WithMemberPrincipals;
import com.qushe8r.expensemanager.budget.dto.PostBudget;
import com.qushe8r.expensemanager.budget.service.BudgetService;
import com.qushe8r.expensemanager.category.dto.PostBudgetCategory;
import com.qushe8r.expensemanager.config.TestSecurityConfig;
import com.qushe8r.expensemanager.macher.MemberDetailsMatcher;
import com.qushe8r.expensemanager.macher.PostBudgetMatcher;
import com.qushe8r.expensemanager.member.entity.MemberDetails;
import java.time.YearMonth;
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

@WebMvcTest(BudgetController.class)
@Import(TestSecurityConfig.class)
class BudgetControllerTest {

  private static final String BUDGET_DEFAULT_URL = "/budgets";

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private BudgetService budgetService;

  @DisplayName("createBudget(): 입력값이 유효하면 성공한다")
  @WithMemberPrincipals
  @Test
  void createBudget() throws Exception {
    // given
    Long createdBudgetId = 1L;
    PostBudgetCategory category = new PostBudgetCategory(1L, "카테고리");
    PostBudget postBudget = new PostBudget(100000L, YearMonth.of(2023, 8), category);
    String content = objectMapper.writeValueAsString(postBudget);

    MemberDetails memberDetails = new MemberDetails(1L, "test@email.com", "password");

    BDDMockito.given(
            budgetService.createBudget(
                Mockito.argThat(new MemberDetailsMatcher(memberDetails)),
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
                .string(HttpHeaders.LOCATION, BUDGET_DEFAULT_URL + "/" + createdBudgetId));
  }
}
