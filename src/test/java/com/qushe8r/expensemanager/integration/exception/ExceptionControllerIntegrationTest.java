package com.qushe8r.expensemanager.integration.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qushe8r.expensemanager.category.repository.CategoryRepository;
import com.qushe8r.expensemanager.config.DataSourceSelector;
import com.qushe8r.expensemanager.expense.dto.PatchExpense;
import com.qushe8r.expensemanager.member.dto.PostMember;
import com.qushe8r.expensemanager.security.jwt.JwtProperties;
import com.qushe8r.expensemanager.security.jwt.TokenProvider;
import com.qushe8r.expensemanager.stub.JwtFactory;
import java.time.LocalDateTime;
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
class ExceptionControllerIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Autowired private JwtProperties jwtProperties;

  @Autowired private DataSourceSelector dataSourceSelector;

  @Autowired private CategoryRepository categoryRepository;

  @DisplayName("methodArgumentNotValidException(): dto 유효성 검사 실패시 예외가 발생한다")
  @Test
  void methodArgumentNotValidException() throws Exception {
    // given
    PostMember postMember = new PostMember("notEmailPattern", "171717171717", false, false);
    String content = objectMapper.writeValueAsString(postMember);

    // when
    ResultActions actions =
        mockMvc.perform(
            MockMvcRequestBuilders.post("/members")
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
        .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors").isArray())
        .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors[0].field").isString())
        .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors[0].rejectedValue").isString())
        .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors[0].reason").isString())
        .andExpect(MockMvcResultMatchers.jsonPath("$.violationErrors").isEmpty());
  }

  @DisplayName("constraintViolationsException(): path 파라미터 유효성 검사 실패시 예외가 발생한다")
  @Test
  void constraintViolationsException() throws Exception {
    // given
    String accessToken = JwtFactory.withDefaultValues().generateToken(jwtProperties);

    PatchExpense patchExpense =
        new PatchExpense(10000L, "김치찌개", LocalDateTime.of(2023, 11, 15, 12, 0), 1L);
    String content = objectMapper.writeValueAsString(patchExpense);

    // when
    ResultActions actions =
        mockMvc.perform(
            MockMvcRequestBuilders.patch("/expenses/-10")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, TokenProvider.BEARER + accessToken)
                .content(content));

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
        .andExpect(MockMvcResultMatchers.jsonPath("$.violationErrors[0].reason").isString());
  }

  @DisplayName("methodNotSupportedException(): 요청의 http 메소드가 잘못되었다면 예외가 발생한다")
  @Test
  void methodNotSupportedException() throws Exception {
    // given
    String accessToken = JwtFactory.withDefaultValues().generateToken(jwtProperties);

    PatchExpense patchExpense =
        new PatchExpense(10000L, "김치찌개", LocalDateTime.of(2023, 11, 15, 12, 0), 1L);
    String content = objectMapper.writeValueAsString(patchExpense);

    // when
    ResultActions actions =
        mockMvc.perform(
            MockMvcRequestBuilders.post("/expenses/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, TokenProvider.BEARER + accessToken)
                .content(content));

    // then
    actions
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isMethodNotAllowed())
        .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").isEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.status").isNumber())
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").isString())
        .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors").isEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.violationErrors").isEmpty());
  }

  @DisplayName("httpMessageNotReadableException(): 요청 본문이 필수인 요청에 본문이 없으면 예외가 발생한다")
  @Test
  void httpMessageNotReadableException() throws Exception {
    // given
    String accessToken = JwtFactory.withDefaultValues().generateToken(jwtProperties);

    // when
    ResultActions actions =
        mockMvc.perform(
            MockMvcRequestBuilders.patch("/expenses/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, TokenProvider.BEARER + accessToken));

    // then
    actions
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").isEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.status").isNumber())
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").isString())
        .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors").isEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.violationErrors").isEmpty());
  }

  @DisplayName("missingServletRequestParameterException(): 필수 파라미터가 없으면 예외가 발생한다")
  @Test
  void missingServletRequestParameterException() throws Exception {
    // given
    String accessToken = JwtFactory.withDefaultValues().generateToken(jwtProperties);

    // when
    ResultActions actions =
        mockMvc.perform(
            MockMvcRequestBuilders.get("/categories/expenses")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, TokenProvider.BEARER + accessToken));

    // then
    actions
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").isEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.status").isNumber())
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").isString())
        .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors").isEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.violationErrors").isEmpty());
  }

  @DisplayName("exception(): 체크되지 않은 예외가 발생하면 500 응답을 한다")
  @Test
  void exception() throws Exception {
    // given
    String accessToken = JwtFactory.withDefaultValues().generateToken(jwtProperties);

    // when
    ResultActions actions =
        mockMvc.perform(
            MockMvcRequestBuilders.post("/exceptions")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, TokenProvider.BEARER + accessToken));

    // then
    actions
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isInternalServerError())
        .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").isEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(500))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Internal Server Error"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors").isEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.violationErrors").isEmpty());
  }
}
