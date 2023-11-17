package com.qushe8r.expensemanager.integration.category;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qushe8r.expensemanager.category.dto.PostCategory;
import com.qushe8r.expensemanager.security.filter.JwtAuthenticationFilter;
import com.qushe8r.expensemanager.security.filter.JwtVerificationFilter;
import com.qushe8r.expensemanager.security.jwt.JwtProperties;
import com.qushe8r.expensemanager.security.jwt.TokenProvider;
import com.qushe8r.expensemanager.stub.JwtFactory;
import java.nio.charset.StandardCharsets;
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
class CategoryControllerIntegrationTest {

  private static final String CATEGORY_NAME = "카테고리";

  private static final String CATEGORY_DEFAULT_URL = "/categories";

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Autowired private JwtProperties jwtProperties;

  @DisplayName("createCategoryTokenNotFound(): 토큰이 없으면 실패한다.")
  @Test
  void createCategoryTokenNotFound() throws Exception {
    // given
    PostCategory postCategory = new PostCategory(CATEGORY_NAME);
    String content = objectMapper.writeValueAsString(postCategory);

    // when
    ResultActions actions =
        mockMvc.perform(
            MockMvcRequestBuilders.post(CATEGORY_DEFAULT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8.name())
                .content(content));

    // then
    actions
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isUnauthorized())
        .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").value("JX03"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(401))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("토큰이 없습니다."))
        .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors").isEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.violationErrors").isEmpty());
  }

  @DisplayName("createCategory(): 정상 입력시 성공한다.")
  @Test
  void createCategory() throws Exception {
    // given
    PostCategory postCategory = new PostCategory(CATEGORY_NAME);
    String content = objectMapper.writeValueAsString(postCategory);
    String accessToken = JwtFactory.withDefaultValues().generateToken(jwtProperties);

    // when
    ResultActions actions =
        mockMvc.perform(
            MockMvcRequestBuilders.post(CATEGORY_DEFAULT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8.name())
                .content(content)
                .header(HttpHeaders.AUTHORIZATION, TokenProvider.BEARER + accessToken));

    // then
    actions
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isCreated());
  }
}
