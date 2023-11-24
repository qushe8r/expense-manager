package com.qushe8r.expensemanager.integration.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qushe8r.expensemanager.config.DataSourceSelector;
import com.qushe8r.expensemanager.member.dto.PatchPassword;
import com.qushe8r.expensemanager.member.dto.PostMember;
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
class MemberControllerIntegrationTest {

  private static final String EMAIL_EXISTS = "test@email.com";

  private static final String EMAIL_EXAMPLE = "test3@email.com";

  private static final String PASSWORD_EXAMPLE = "c2f9x9@43a";

  private static final String MEMBER_DEFAULT_URL = "/members";

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Autowired private JwtProperties jwtProperties;

  @Autowired private DataSourceSelector dataSourceSelector;

  @BeforeEach
  void setUp() {
    dataSourceSelector.toWrite();
  }

  @DisplayName("createMember(): 입력값이 유효하면 회원가입에 성공한다")
  @Test
  void createMember() throws Exception {
    // given
    PostMember postMember = new PostMember(EMAIL_EXAMPLE, PASSWORD_EXAMPLE, false, false);
    String content = objectMapper.writeValueAsString(postMember);

    // when
    ResultActions actions =
        mockMvc.perform(
            MockMvcRequestBuilders.post(MEMBER_DEFAULT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content));

    // then
    actions
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.header().exists(HttpHeaders.LOCATION));
  }

  @DisplayName("createMemberMemberAlreadyExistsException(): 이미 회원으로 존재하는 아이디를 입력하면 예외가 발생한다")
  @Test
  void createMemberMemberAlreadyExistsException() throws Exception {
    // given
    PostMember postMember = new PostMember(EMAIL_EXISTS, PASSWORD_EXAMPLE, false, false);
    String content = objectMapper.writeValueAsString(postMember);

    // when
    ResultActions actions =
        mockMvc.perform(
            MockMvcRequestBuilders.post(MEMBER_DEFAULT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content));

    // then
    actions
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isConflict())
        .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").value("MX02"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(409))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("이미 존재하는 회원입니다."))
        .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors").isEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.violationErrors").isEmpty());
  }

  @DisplayName("modifyPassword(): 입력값이 유효하면 성공한다.")
  @Test
  void modifyPassword() throws Exception {
    // given
    PatchPassword patchPassword = new PatchPassword("A*czkax8c!");
    String content = objectMapper.writeValueAsString(patchPassword);
    String accessToken = JwtFactory.withDefaultValues().generateToken(jwtProperties);

    // when
    ResultActions actions =
        mockMvc.perform(
            MockMvcRequestBuilders.patch(MEMBER_DEFAULT_URL + "/password")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, TokenProvider.BEARER + accessToken)
                .content(content));

    // then
    actions.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk());
  }
}
