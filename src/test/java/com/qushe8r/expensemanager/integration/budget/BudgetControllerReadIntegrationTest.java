package com.qushe8r.expensemanager.integration.budget;

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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class BudgetControllerReadIntegrationTest {

  private static final String BUDGET_DEFAULT_URL = "/budgets";

  @Autowired private MockMvc mockMvc;

  @Autowired private JwtProperties jwtProperties;

  @Autowired private DataSourceSelector dataSourceSelector;

  @BeforeEach
  void setUp() {
    dataSourceSelector.toRead();
  }

  @DisplayName("getRecommendation(): 존재하지 않는 id를 입력해도 noContent 상태코드만 응답한다.")
  @Test
  void getRecommendation() throws Exception {
    // given
    String accessToken = JwtFactory.withDefaultValues().generateToken(jwtProperties);
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("amount", "1000000");

    // when
    ResultActions actions =
        mockMvc.perform(
            MockMvcRequestBuilders.get(BUDGET_DEFAULT_URL + "/recommendation")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .params(params)
                .header(HttpHeaders.AUTHORIZATION, TokenProvider.BEARER + accessToken));

    // then
    actions.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk());
  }
}
