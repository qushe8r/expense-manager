package com.qushe8r.expensemanager.integration.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qushe8r.expensemanager.config.DataSourceSelector;
import com.qushe8r.expensemanager.security.dto.UsernamePassword;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.cookies.CookieDocumentation;
import org.springframework.restdocs.headers.HeaderDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@AutoConfigureRestDocs
class SecurityIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Autowired private DataSourceSelector dataSourceSelector;

  @BeforeEach
  void setUp() {
    dataSourceSelector.toWrite();
  }

  @DisplayName("login(): 입력값이 유효하면 로그인에 성공한다.")
  @Test
  void login() throws Exception {
    // given
    UsernamePassword usernamePassword = new UsernamePassword("test@email.com", "password");
    String content = objectMapper.writeValueAsString(usernamePassword);

    // when
    ResultActions actions =
        mockMvc.perform(
            RestDocumentationRequestBuilders.post("/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content));

    // then
    actions
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.header().exists(HttpHeaders.AUTHORIZATION))
        .andExpect(MockMvcResultMatchers.header().exists(HttpHeaders.SET_COOKIE))
        .andDo(
            MockMvcRestDocumentation.document(
                "post-sign-in",
                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                PayloadDocumentation.requestFields(
                    PayloadDocumentation.fieldWithPath("email").description("이메일(아이디)"),
                    PayloadDocumentation.fieldWithPath("password").description("변경할 비밀번호")),
                HeaderDocumentation.responseHeaders(
                    HeaderDocumentation.headerWithName(HttpHeaders.AUTHORIZATION)
                        .description("액세스 토큰")),
                CookieDocumentation.responseCookies(
                    CookieDocumentation.cookieWithName("Refresh").description("리프레시 토큰"))));
    ;
  }
}
