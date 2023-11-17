package com.qushe8r.expensemanager.integration.category;

import com.qushe8r.expensemanager.config.DataSourceSelector;
import com.qushe8r.expensemanager.security.jwt.JwtProperties;
import com.qushe8r.expensemanager.security.jwt.TokenProvider;
import com.qushe8r.expensemanager.stub.JwtFactory;
import java.time.LocalDate;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@SpringBootTest
@AutoConfigureMockMvc
class CategoryControllerReadIntegrationTest {

  private static final String CATEGORY_DEFAULT_URL = "/categories";

  @Autowired private MockMvc mockMvc;

  @Autowired private JwtProperties jwtProperties;

  @Autowired private DataSourceSelector dataSourceSelector;

  @BeforeEach
  void setUp() {
    dataSourceSelector.toRead();
  }

  @DisplayName("getCategories(): 조회 성공시 카테고리 목록을 반환한다.")
  @Test
  void getCategories() throws Exception {
    // given
    String accessToken = JwtFactory.withDefaultValues().generateToken(jwtProperties);

    // when
    ResultActions actions =
        mockMvc.perform(
            MockMvcRequestBuilders.get(CATEGORY_DEFAULT_URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, TokenProvider.BEARER + accessToken));

    // then
    actions
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").isNumber())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name").isString());
  }

  @DisplayName("getCategorizedExpense(): 조회 성공시 GlobalTotalsExpenseResponse(dto)를 반환한다.")
  @Test
  void getCategorizedExpense() throws Exception {
    // given
    String accessToken = JwtFactory.withDefaultValues().generateToken(jwtProperties);

    LocalDate start = LocalDate.of(2023, 11, 1);
    LocalDate end = LocalDate.of(2023, 11, 30);
    Long min = 10000L;
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
                .params(params)
                .header(HttpHeaders.AUTHORIZATION, TokenProvider.BEARER + accessToken));

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
            MockMvcResultMatchers.jsonPath("$.data.categories[0].expenses[0].expenseAt")
                .isString());
  }
}
