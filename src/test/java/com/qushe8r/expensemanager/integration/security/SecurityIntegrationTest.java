package com.qushe8r.expensemanager.integration.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qushe8r.expensemanager.config.DataSourceSelector;
import com.qushe8r.expensemanager.security.dto.UsernamePassword;
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
class SecurityIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Autowired private DataSourceSelector dataSourceSelector;

  @BeforeEach
  void setUp() {
    dataSourceSelector.toWrite();
  }

  @DisplayName("createMember(): 입력값이 유효하면 회원가입에 성공한다")
  @Test
  void createMember() throws Exception {
    // given
    UsernamePassword usernamePassword = new UsernamePassword("test@email.com", "qazsedc12#");
    String content = objectMapper.writeValueAsString(usernamePassword);

    // when
    ResultActions actions =
        mockMvc.perform(
            MockMvcRequestBuilders.post("/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content));

    // then
    actions
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.header().exists(HttpHeaders.AUTHORIZATION))
        .andExpect(MockMvcResultMatchers.header().exists(HttpHeaders.SET_COOKIE));
  }
}
